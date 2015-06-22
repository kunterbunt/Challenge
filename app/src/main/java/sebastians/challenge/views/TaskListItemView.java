package sebastians.challenge.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import sebastians.challenge.R;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.Task;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class TaskListItemView extends View {

    private Challenge challenge;

    private int width;
    private int height;
    private int dueTaskId;
    private int circRadius;
    private boolean inited = false;
    Paint inactivePaint;
    Paint notDonePaint;
    Paint donePaint;
    Paint currentPaint;


    int totWidth;
    private ArrayList<PointF> itemsPositions = new ArrayList<>();


    public TaskListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public void init(){
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint.setStyle(Paint.Style.FILL);
        inactivePaint.setColor(Color.rgb(100, 100, 100));

        notDonePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        notDonePaint.setStyle(Paint.Style.STROKE);
        notDonePaint.setStrokeWidth(circRadius / 5);
        notDonePaint.setColor(Color.rgb(255, 0, 0));

        donePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        donePaint.setStyle(Paint.Style.STROKE);
        donePaint.setStrokeWidth(circRadius / 5);
        donePaint.setColor(Color.rgb(0, 0, 0));

        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setStyle(Paint.Style.FILL);
        currentPaint.setColor(getResources().getColor(R.color.app_color_accent));

        dueTaskId = challenge.getDueTaskId();
        circRadius = circleSizeCalculation();
        totWidth = circRadius * 3 * challenge.getTaskList().size();

        int itemX = (width - totWidth) / 2;
        int itemY = height / 2;

        for(int i = 0; i < challenge.getTaskList().size(); i++){
            Task task = challenge.getTaskList().get(i);
            PointF curPoint = new PointF();
            if(i > 0)
                itemX += circRadius * 3;

            curPoint.x = itemX;
            curPoint.y = itemY;
            itemsPositions.add(curPoint);

        }

        //calculate points for circles!

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(challenge == null)
            return;



        if(!inited){
            width = canvas.getWidth();
            height = canvas.getHeight();
            this.init();
            inited = true;
        }


        for(int i = 0; i < challenge.getTaskList().size(); i++){
            PointF curPoint = itemsPositions.get(i);
            int mRadius = circRadius + (int)((((double)circRadius) / 2 ));

            if(challenge.isActive()) {

                if (i == dueTaskId) {
                    canvas.drawCircle(curPoint.x, curPoint.y, mRadius, currentPaint);


                }else if(i < dueTaskId) {

                    if (challenge.getTaskList().get(i).isDone()) {
                        canvas.drawCircle(curPoint.x, curPoint.y, mRadius, donePaint);
                    } else {
                        canvas.drawCircle(curPoint.x, curPoint.y, mRadius, notDonePaint);
                    }

                }else{
                    canvas.drawCircle(curPoint.x, curPoint.y, mRadius, inactivePaint);
                }

            }  else {
                canvas.drawCircle(curPoint.x, curPoint.y, mRadius, inactivePaint);
            }


            //is this point closest to all others?

        }


    }

    public int circleSizeCalculation(){
        int radius = height / 4;
        int mWidth;
        do {
            radius--;

            mWidth = radius * 3 * challenge.getTaskList().size();

        }while(mWidth > width);

        return radius;
    }




    public void setChallenge(Challenge challenge){
        this.challenge = challenge;
    }



}
