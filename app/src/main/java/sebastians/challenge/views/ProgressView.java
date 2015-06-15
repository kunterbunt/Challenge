package sebastians.challenge.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import sebastians.challenge.R;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.Task;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ProgressView extends View {

    private Challenge challenge;

    private PointF touchPoint = new PointF();
    private int width;
    private int height;
    private int dueTaskId;
    private int circRadius;
    private boolean inited = false;
    Paint inactivePaint;
    Paint selectedPaint;
    Paint currentPaint;
    Paint donePaint;
    private ArrayList<PointF> itemsPositions = new ArrayList<>();


    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public void init(){
        circRadius = this.circleSizeCalculation();
        inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        inactivePaint.setStyle(Paint.Style.FILL);
        inactivePaint.setColor(Color.rgb(100, 100, 100));
        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(circRadius / 5);
        selectedPaint.setColor(Color.rgb(0, 0, 0));

        currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentPaint.setStyle(Paint.Style.FILL);
        currentPaint.setColor(getResources().getColor(R.color.app_color_accent));

        dueTaskId = challenge.getDueTaskId();


        int totWidth = circRadius * 3 * challenge.getTaskList().size();

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

    public double distanceToScale(PointF p1, PointF p2){
        double maxDist = circRadius * 4;
        double calcDist;
        Point result = new Point();
        double yDist = Math.abs (p1.y - p1.y);
        double xDist = Math.abs (p1.x - p2.x);
        calcDist = Math.sqrt((yDist)*(yDist) +(xDist)*(xDist));

        if(calcDist > maxDist)
            return 0;
        return (1 - calcDist / maxDist);



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



        int selectedPointId = getClosestPointIdToTouch();
        for(int i = 0; i < challenge.getTaskList().size(); i++){
            PointF curPoint = itemsPositions.get(i);
            double influence = distanceToScale(curPoint, touchPoint);
            int mRadius = circRadius + (int)((((double)circRadius) / 2 )* influence);
            int mRadiusWOInf = circRadius + (int)((((double)circRadius) / 2 ));

            if(selectedPointId == i){
                mRadius = mRadiusWOInf;
            }

            if(i == dueTaskId){
                canvas.drawCircle(curPoint.x, curPoint.y, mRadius , currentPaint);
            }else{
                canvas.drawCircle(curPoint.x, curPoint.y, mRadius , inactivePaint);
            }



            //is this point closest to all others?
            if(selectedPointId == i){
                canvas.drawCircle(curPoint.x, curPoint.y, 2 + mRadiusWOInf, selectedPaint);
            }
        }

    }



    public int getClosestPointIdToTouch(){
        double maxInfluence = -1;
        int savedIdx = -1;
        for(int i = 0; i < itemsPositions.size(); i++){
            double influence = distanceToScale(itemsPositions.get(i),touchPoint);
            if(maxInfluence < influence){
                savedIdx = i;
                maxInfluence = influence;
            }
        }
        return savedIdx;
    }

    public int circleSizeCalculation(){
        int radius = height / 4;
        int mWidth;
        do {
            radius--;
            int distance = radius * 2;
            int items = challenge.getTaskList().size();

            mWidth = radius * items * (items);

        }while(mWidth > width);

        return radius;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchPoint.x = event.getX();
        touchPoint.y = event.getY();

        //redraw active
        this.invalidate();
        super.onTouchEvent(event);

        return true;

    }

    public void setChallenge(Challenge challenge){
        this.challenge = challenge;
    }



}
