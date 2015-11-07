package com.raven.game2048;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Created by cyend on 2015/11/7.
 *
 */
public class GameView extends GridLayout {

    private Card[][] cards = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public void initGameView() {
        setColumnCount(4);

        setOnTouchListener(new OnTouchListener() {
            private double startX, startY;
            private double offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                Log.i("MoveTo", "Left");
                                swipeLeft();
                                addRandomNumber();
                            } else if (offsetX > 5) {
                                Log.i("MoveTo", "Right");
                                swipeRight();
                                addRandomNumber();
                            }
                        } else {
                            if (offsetY < -5) {
                                Log.i("MoveTo", "Up");
                                swipeUp();
                                addRandomNumber();
                            } else if (offsetY > 5) {
                                Log.i("MoveTo", "Down");
                                swipeDown();
                                addRandomNumber();
                            }
                        }
                        break;
                }

                return true;
            }
        });
    }

    private void addScore(Card c) {
        int v = c.getNumber();
        MainActivity.getMainActivity().addScore(v);
    }

    private void addRandomNumber() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cards[x][y].getNumber() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        if (emptyPoints.size() == 0) return;
        Date d = new Date();
        Random r = new Random(d.getTime());
        Point p = emptyPoints.remove(r.nextInt(emptyPoints.size()));
        cards[p.x][p.y].setNumber(r.nextInt(6) > 1 ? 2 : 4);
    }

    private void startGame() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cards[x][y].setNumber(0);
            }
        }
        addRandomNumber();
        addRandomNumber();
    }

    private void swipeLeft() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int i = x + 1; i < 4; i++) {
                    if (cards[i][y].getNumber() > 0) {
                        Log.i("Cards", "(" + x + ", " + y + ") <- (" + i + ", " + y + ")");
                        if (cards[x][y].getNumber() <= 0) {
                            cards[x][y].setNumber(cards[i][y].getNumber());
                            cards[i][y].setNumber(0);
                            x--;
                            break;
                        }
                        else if (cards[x][y].equalNumber(cards[i][y])) {
                            cards[x][y].setNumber(cards[x][y].getNumber() * 2);
                            cards[i][y].setNumber(0);
                            addScore(cards[x][y]);
                        }
                    }
                }
            }
        }
    }
    private void swipeRight() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int i = x - 1; i >= 0; i--) {
                    if (cards[i][y].getNumber() > 0) {
                        if (cards[x][y].getNumber() <= 0) {
                            cards[x][y].setNumber(cards[i][y].getNumber());
                            cards[i][y].setNumber(0);
                            x++;
                            break;
                        }
                        else if (cards[x][y].equalNumber(cards[i][y])) {
                            cards[x][y].setNumber(cards[x][y].getNumber() * 2);
                            cards[i][y].setNumber(0);
                            addScore(cards[x][y]);
                        }
                    }
                }
            }
        }
    }
    private void swipeUp() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int i = y + 1; i < 4; i++) {
                    if (cards[x][i].getNumber() > 0) {
                        if (cards[x][y].getNumber() <= 0) {
                            cards[x][y].setNumber(cards[x][i].getNumber());
                            cards[x][i].setNumber(0);
                            y--;
                            break;
                        }
                        else if (cards[x][y].equalNumber(cards[x][i])) {
                            cards[x][y].setNumber(cards[x][y].getNumber() * 2);
                            cards[x][i].setNumber(0);
                            addScore(cards[x][y]);
                        }
                    }
                }
            }
        }
    }
    private void swipeDown() {
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int i = y - 1; i >= 0; i--) {
                    if (cards[x][i].getNumber() > 0) {
                        if (cards[x][y].getNumber() <= 0) {
                            cards[x][y].setNumber(cards[x][i].getNumber());
                            cards[x][i].setNumber(0);
                            y++;
                            break;
                        }
                        else if (cards[x][y].equalNumber(cards[x][i])) {
                            cards[x][y].setNumber(cards[x][y].getNumber() * 2);
                            cards[x][i].setNumber(0);
                            addScore(cards[x][y]);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w, h) - 10) / 4;
        int cardHeight = cardWidth;
        addCards(cardWidth, cardHeight);
        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                Card c = new Card(getContext());
                c.setNumber(0);
                Log.i("AddCards", c.toString());
                addView(c, cardWidth, cardHeight);
                cards[x][y] = c;
            }
        }
    }

}
