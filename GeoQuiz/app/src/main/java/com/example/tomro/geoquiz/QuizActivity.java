package com.example.tomro.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX_CHEATER = "cheater_index";
    private static final String KEY_INDEX_CHEATS_REMAINING = "cheats_remaining_index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mCheatsRemainingTextView;

    private boolean mIsCheater;
    private int mCheatsRemaining;



    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    }; //should be elsewhere but is placed in controller for purposes of this exercise

    private int mCurrentIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mCheatsRemainingTextView = (TextView) findViewById(R.id.cheat_left_text_view);




        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                checkAnswer(true);
                checkForQuizCompleted();

            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                checkAnswer(false);
                checkForQuizCompleted();


            }
        });
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateQuestion();



            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();
                startActivityForResult(CheatActivity.newIntent(QuizActivity.this, answerIsTrue), REQUEST_CODE_CHEAT);



            }
        });

        Log.d(TAG, "onCreate (Bundle) called");
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_INDEX)) {
            this.mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());

        } else {
            this.mCurrentIndex=mQuestionBank.length-1; //sets initial question to final one in array so that it rolls over at program start
            updateQuestion();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_INDEX_CHEATER)) {
            this.mIsCheater = savedInstanceState.getBoolean(KEY_INDEX_CHEATER);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_INDEX_CHEATS_REMAINING)) {
            this.mCheatsRemaining = savedInstanceState.getInt(KEY_INDEX_CHEATS_REMAINING);
        } else {
            this.mCheatsRemaining = 3;
        }

        updateCheatsRemaining();




    }

    private void updateCheatsRemaining() {
        if (this.mCheatsRemaining == 0) {
            mCheatButton.setEnabled(false);
        } else {
            mCheatButton.setEnabled(true);
        }
        this.mCheatsRemainingTextView.setText("" + mCheatsRemaining);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "onSaveInstanceState() called");
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_INDEX_CHEATER, mIsCheater);
        outState.putInt(KEY_INDEX_CHEATS_REMAINING, mCheatsRemaining);
    }



    private void updateQuestion() {
        if (mCurrentIndex < mQuestionBank.length - 1) {
            mCurrentIndex++;
        } else {
            mCurrentIndex = 0;
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkIfCompleted(mCurrentIndex);
        updateCheatsRemaining();
    }

    private void previousQuestion() {
        if (mCurrentIndex > 0) {
            mCurrentIndex--;
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        checkIfCompleted(mCurrentIndex);
        updateCheatsRemaining();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerToQuestion = mQuestionBank[mCurrentIndex].getAnswerTrue();

        if (mIsCheater) {
            Toast.makeText(this, R.string.judgement_toast, Toast.LENGTH_SHORT).show();
            mQuestionBank[mCurrentIndex].setQuestionCompleted();
            mQuestionBank[mCurrentIndex].setUserCorrect(false);
            mCheatsRemaining--;
            mIsCheater = false; //sets cheating status back to false after question is marked false

            checkIfCompleted(mCurrentIndex);
            return;
        }

        if (userPressedTrue == answerToQuestion) {
            Toast.makeText(this, R.string.correct_toast,Toast.LENGTH_SHORT).show();
            mQuestionBank[mCurrentIndex].setQuestionCompleted();
            mQuestionBank[mCurrentIndex].setUserCorrect(true);
        } else {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
            mQuestionBank[mCurrentIndex].setQuestionCompleted();
            mQuestionBank[mCurrentIndex].setUserCorrect(false);
        }

        checkIfCompleted(mCurrentIndex);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                this.mIsCheater = CheatActivity.wasAnswerShown(data);
            }
        }


    }

    private void checkIfCompleted(int currentIndex) {
        if (this.mQuestionBank[currentIndex].getQuestionCompleted()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    private void checkForQuizCompleted() { //needs fixing
        int correct = 0;
        int total = 0;
        for (Question q : this.mQuestionBank) {
            if (!q.getQuestionCompleted()) {
                return;
            }
        }
        for (Question q : this.mQuestionBank) {

            total++;
            if (q.getUserCorrect()) {
                correct++;
            }
            int finalScore = (correct/total) * 100;
            System.out.println(finalScore);
            Toast.makeText(QuizActivity.this , R.string.quiz_complete_toast, Toast.LENGTH_LONG ); // fix

        }
    }


}
