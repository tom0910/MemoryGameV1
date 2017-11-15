package hu.zeffer.memorygame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import java.util.Random
import android.graphics.Color
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler
import android.util.TypedValue
import android.widget.GridView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enbaleStartButton()
        initMsgs()
    }

    val tileArray = listOf(R.id.A0,R.id.A1,R.id.A2,R.id.A3,R.id.A4,R.id.B0,R.id.B1,R.id.B2,R.id.B3,R.id.B4,R.id.C0,R.id.C1,R.id.C2,R.id.C3,R.id.C4,R.id.D0,R.id.D1,R.id.D2,R.id.D3,R.id.D4,R.id.E0,R.id.E1,R.id.E2,R.id.E3,R.id.E4)
    var bombs=ArrayList<Button>()
    val randomizeBomb = Random()
    var good: Int = 0
    var bad: Int = 0
    var numBombs: Int = 0
    var totalEarnedPoints:Int = 0
    var gameNumber:Int = 0

    fun msgTop() {
        val msg:TextView = findViewById(R.id.msgTop)
        msg.setText("GAME:$gameNumber")
    }
    fun initMsgs() {
        msgTop()
    }
    fun initButtons() {
        for(id:Int in tileArray) {
            var but :Button = findViewById(id)
            but.setBackgroundColor(Color.TRANSPARENT)
            but.setBackgroundColor(Color.BLUE)
            but.isEnabled=true
            but.setText("")
        }
    }
    fun initVars() {
        good = 0
        bad = 0
        numBombs = 0
        bombs= arrayListOf()
    }

    public fun SClicked(view: View) {
        val startButton = view as Button
        val handler = Handler()
        gameNumber++
        initButtons()
        initVars()
        initMsgs()
        randBombs()
        showBombs()
        startButton.isEnabled=false
        numBombs = bombs.size
        handler.postDelayed({ clearFields() }, 1000)
    }

    fun showBombs(){
        for( b:Button in bombs) {
            b.setBackgroundColor(Color.GREEN)
        }
    }

    fun randBombs(){
        var indexes=ArrayList<Int>()
        val rnd = Random()
        do{
            var r:Int = rnd.nextInt(25)
            if ( !indexes.contains(r)) {
                indexes.add(r)
            }
        }while(indexes.size != 5)

        for(i in 0..indexes.size-1) {
            var but :Button = findViewById(tileArray.get(indexes[i]));
            bombs.add(but);

        }

    }

    fun clearFields() {
        for(but in bombs) {
            but.setBackgroundColor(Color.BLUE)
        }
    }

    var playedTilesIds=ArrayList<Int>()



    fun markSteps(butStepped: Button) {
        val msg: TextView = findViewById(R.id.messageRes)

        if (bombs.contains(butStepped)) {
            butStepped.setBackgroundColor(Color.GREEN)
            butStepped.setTextColor(Color.WHITE)
            butStepped.text = "\u2705"
            butStepped.isEnabled = false
            good++;
        } else {
            butStepped.setBackgroundColor(Color.RED)
            butStepped.setTextColor(Color.WHITE)
            butStepped.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            butStepped.text = "\u2639"
            butStepped.isEnabled = false
            bad++
        }
        playedTilesIds.add(butStepped.id)

        msg.setText("You found $good / $numBombs. Missed total of $bad")
        if(good.equals(bombs.size)) {
            endOfGame()
        } else {
            Toast.makeText(this, "You found $good / $numBombs. Missed total of $bad", Toast.LENGTH_SHORT).show()
        }
    }

    fun endOfGame() {
        var points: Int = good-bad
        totalEarnedPoints += points
        var msg: TextView = findViewById(R.id.messageRes)
        var msgTotal: TextView = findViewById(R.id.msgTotal)
        msg.setText("The end of this game. In this game, you earned $points points")
        msgTotal.setText("TOTAL: $totalEarnedPoints out of $gameNumber games")

        Toast.makeText(this, "Game is Over!", Toast.LENGTH_SHORT).show()
        showGreen()
        enbaleStartButton()
    }
    fun showGreen() {
        for(butId in tileArray) { //do for every button
            val but: Button = findViewById(butId)
            but.isEnabled = false
            for (playedId in playedTilesIds) {//get all the played buttons
                if (!tileArray.contains(playedId)) { // if a button is not played
                    but.setBackgroundColor(Color.BLUE)
                    but.setTextColor(Color.WHITE)
                }
                Toast.makeText(this, "butId:$butId and playedId:$playedId", Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected fun BClicked(view:View){
        val buttonSelected: Button = view as Button;
        markSteps(buttonSelected)
    }
    private fun enbaleStartButton() {
        val but: Button = findViewById(R.id.startGame);
        but.isEnabled=true
        but.setText("New Game")
    }
}
