package edu.umb.cs443.hw2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {

    GridView gridView;
    private static int w = 5,curx,cury,currentPos,prevI, prevJ, i, j;
    private Random r=new Random();
    static String[] tiles = new String[w*w];
    private Thread thread;
    private int XOnBoard = 0;
    private int cells = 0;
    private int treasure = 0;
    private int cellCount = 0;
    private int treasureCount = 0;
    private TextView cellView;
    private TextView treasureView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, tiles);

        gridView.setAdapter(adapter);
        init();
        generateX(gridView);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                cellView = findViewById(R.id.textCell);
                treasureView = findViewById(R.id.textTreasure);
                prevJ = j;
                prevI = i;
                i = getI(position);
                j = getJ(position);
                moveToClick(gridView);
                //generateX(gridView);
                    Log.d("PREV COORDINATES", "( " + String.valueOf(prevI) + "," + String.valueOf(prevJ) + " )");
                    Log.d("COORDINATES", "( " + String.valueOf(i) + "," + String.valueOf(j) + " )");
                    Log.d("CURRENT POS MT", String.valueOf(currentPos));
                    Log.d("POSITION", String.valueOf(position));
                    Log.d("X on the board", String.valueOf(XOnBoard));


                    Toast.makeText(getApplicationContext(),
                        (CharSequence) (new Integer(position).toString()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateX(View view){
        thread = new Thread(generate);
        thread.start();
    }

    private void moveToClick(View view){
        thread = new Thread(navigate);
        thread.start();

    }

    public Handler generateHandler = new Handler(){
        public void handleMessage(android.os.Message message){
             if(XOnBoard < 5){
                 curx=r.nextInt(w);
                 cury=r.nextInt(w);
                 while(tiles[curx * w + cury] == "X" || tiles[curx * w + cury] == "0"){
                     curx=r.nextInt(w);
                     cury=r.nextInt(w);
                 }
                 tiles[curx * w + cury] = "X";

                 ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
                 XOnBoard++;
            }

        }
    };

    public Handler navigateHandler = new Handler(){

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message message) {
            Log.d("HANDLERR::::::", String.valueOf(" prevI : " + String.valueOf(prevI) + " I: " + String.valueOf(i)+ " prevJ: " +  String.valueOf(prevJ) + " J: " + String.valueOf(j)));
            Log.d("in the handler", "( " + i + "," + String.valueOf(j) + " )");

            if (prevI != i) {
                cellCount++;
                if (prevI < i) {
                    tiles[prevI * w + prevJ] = "";
                    if(tiles[++prevI * w + prevJ] == "X"){
                        treasureCount++;
                        XOnBoard--;
                    }
                    tiles[prevI * w + prevJ] = "0";
                    cellView.setText(cellCount + " Cells");
                    treasureView.setText(treasureCount + " Treasures");
                    ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
                    return;
                } else {
                    tiles[prevI * w + prevJ] = "";
                    if(tiles[--prevI * w + prevJ] == "X"){
                        treasureCount++;
                        XOnBoard--;
                    }
                    tiles[prevI * w + prevJ] = "0";
                    cellView.setText(cellCount + " Cells");
                    treasureView.setText(treasureCount + " Treasures");
                    ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
                    return;
                }
            }

            if (prevJ != j) {
                cellCount++;
                if (prevJ < j) {
                    tiles[prevI * w + prevJ] = "";
                    if(tiles[prevI * w + ++prevJ]== "X"){
                        treasureCount++;
                        XOnBoard--;
                    }
                    tiles[prevI * w + prevJ] = "0";
                    cellView.setText(cellCount + " Cells");
                    treasureView.setText(treasureCount + " Treasures");
                    ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
                    return;
                } else {
                    tiles[prevI * w + prevJ] = "";
                    if(tiles[prevI * w + --prevJ] == "X"){
                        treasureCount++;
                        XOnBoard--;
                    }
                    tiles[prevI * w + prevJ] = "0";
                    cellView.setText(cellCount + " Cells");
                    treasureView.setText(treasureCount + " Treasures");
                    ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
                    return;
                }
            }

            if(prevI == i && prevJ == j){
                thread.interrupt();
            }
        }

    };


    private Runnable generate = new Runnable() {
        private static final int DELAY = 1000;
        @Override
        public void run() {
            try{
                while(true){
                    generateHandler.sendEmptyMessage(0);
                    Thread.sleep (DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable navigate = new Runnable() {
        private static final int DELAY = 500;
        @Override
        public void run() {
            try{
                while(true){
                    navigateHandler.sendEmptyMessage(0);
                    Thread.sleep (DELAY);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public void reset(View view){
        init();
        generateX(gridView);
    }

    private int getI(int id){
        return id / 5;
    }

    private int getJ(int id){
        int row = id / 5;
        return id - (row * 5);
    }

    void init(){
        cellView = findViewById(R.id.textCell);
        treasureView= findViewById(R.id.textTreasure);

        XOnBoard = 0;
        cellCount = 0;
        treasureCount = 0;
        for(int i=0;i<tiles.length;i++) tiles[i]=" ";
        curx=r.nextInt(w);
        cury=r.nextInt(w);
        currentPos = curx * w + cury;
        i = getI(currentPos);
        j = getJ(currentPos);
        tiles[currentPos] = "0";
        cellView.setText("0 Cells");
        treasureView.setText("0 Treasures");
        ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }


}
