package com.umik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static LangkahPuzzle mGridView;

    private static final int COLUMNS = 4;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;

    private static int mColumnWidth, mColumnHeight;

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";

    private static String[] titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        scramble();

        setDimensions();
    }

    private void init() {
        mGridView = (LangkahPuzzle) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);

        titleList = new String[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            titleList[i] = String.valueOf(i);
        }
    }

    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = titleList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = titleList[index];
            titleList[index] = titleList[i];
            titleList[i] = temp;
        }
    }

    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / COLUMNS;

                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private static void display(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i = 0; i < titleList.length; i++) {
            button = new Button(context);

            if (titleList[i].equals("0"))
                button.setBackgroundResource(R.drawable.a);
            else if (titleList[i].equals("1"))
                button.setBackgroundResource(R.drawable.b);
            else if (titleList[i].equals("2"))
                button.setBackgroundResource(R.drawable.c);
            else if (titleList[i].equals("3"))
                button.setBackgroundResource(R.drawable.d);
            else if (titleList[i].equals("4"))
                button.setBackgroundResource(R.drawable.e);
            else if (titleList[i].equals("5"))
                button.setBackgroundResource(R.drawable.f);
            else if (titleList[i].equals("6"))
                button.setBackgroundResource(R.drawable.g);
            else if (titleList[i].equals("7"))
                button.setBackgroundResource(R.drawable.h);
            else if (titleList[i].equals("8"))
                button.setBackgroundResource(R.drawable.i);
            else if (titleList[i].equals("9"))
                button.setBackgroundResource(R.drawable.j);
            else if (titleList[i].equals("10"))
                button.setBackgroundResource(R.drawable.k);
            else if (titleList[i].equals("11"))
                button.setBackgroundResource(R.drawable.l);
            else if (titleList[i].equals("12"))
                button.setBackgroundResource(R.drawable.m);
            else if (titleList[i].equals("13"))
                button.setBackgroundResource(R.drawable.n);
            else if (titleList[i].equals("14"))
                button.setBackgroundResource(R.drawable.o);
            buttons.add(button);

        }

        mGridView.setAdapter(new CustomPuzzle(buttons, mColumnWidth, mColumnHeight));
    }

    private static void swap(Context context, int currentPosition, int swap) {
        String newPosition = titleList[currentPosition + swap];
        titleList[currentPosition + swap] = titleList[currentPosition];
        titleList[currentPosition] = newPosition;
        display(context);

        if (isSolved()) Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
    }

    public static void moveTiles(Context context, String direction, int position) {

        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS &&
                position % COLUMNS == 0) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= DIMENSIONS - COLUMNS - 1) swap(context, position,
                        COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context, position, COLUMNS);
        }
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < titleList.length; i++) {
            if (titleList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }
}
