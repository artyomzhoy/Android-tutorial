package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String ANSWER_INDEX = "answer_index";
    private static final String RIGHT_ANSWERS = "right_answers";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String IS_CHEATER = "is_cheater";
    private static final String TEST = "test";


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private int rightAnswers = 0;
    private boolean mIsCheater = false;

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (mQuestionBank[mCurrentIndex].isQuestionAnswered()) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
            mCheatButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
            mCheatButton.setEnabled(true);

        }
    }


    private void checkAnswer(boolean userChoice) {
        boolean answerIsRight = mQuestionBank[mCurrentIndex].isRightAnswer();
        int messageResId = 0;
            if (mIsCheater) {
                //mQuestionBank[mCurrentIndex].setUserCheat(true);
                messageResId = R.string.judgement_toast;
            } else {
                if (userChoice == answerIsRight) {
                    messageResId = R.string.correct_toast;
                    rightAnswers += 1;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }

        mFalseButton.setEnabled(false);
        mTrueButton.setEnabled(false);
        mCheatButton.setEnabled(false);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void result () {
        int answers = 0;

        for (int i = 0; i < mQuestionBank.length; i++) {
            if (mQuestionBank[i].isQuestionAnswered()) {
                answers += 1;
            }
        }
        if (answers == mQuestionBank.length) {
            String text = getString(R.string.right_answers_percent_toast);
            String percent = getString(R.string.percent);
            int value = (rightAnswers * 100 / mQuestionBank.length);
            String val = Integer.toString(value);
            String result = text + " " + val + percent;
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            rightAnswers = savedInstanceState.getInt(RIGHT_ANSWERS, 0);
            for (int i = 0; i < mQuestionBank.length; i++) {
                boolean answered = savedInstanceState.getBoolean(ANSWER_INDEX + i);
                mQuestionBank[i].setQuestionAnswered(answered);
            }
            for (int i = 0; i < mQuestionBank.length; i++) {
                boolean cheated = savedInstanceState.getBoolean(IS_CHEATER, false);
                mQuestionBank[i].setUserCheat(cheated);
                }
            mIsCheater = savedInstanceState.getBoolean(TEST, false);
            }

        mQuestionTextView = (TextView) findViewById(R.id.TextView);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                    mIsCheater = mQuestionBank[mCurrentIndex].isUserCheat();

                updateQuestion();
                result();
            }
        });

        mFalseButton = (Button) findViewById(R.id.ButtonFalse);
            mFalseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        checkAnswer(false);
                        mQuestionBank[mCurrentIndex].setQuestionAnswered(true);

                }
        });

         mTrueButton = (Button) findViewById(R.id.ButtonTrue);
            mTrueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        checkAnswer(true);
                        mQuestionBank[mCurrentIndex].setQuestionAnswered(true);

                    }
            });

         mCheatButton = (Button) findViewById(R.id.ButtonCheat);
            mCheatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isRightAnswer();
                    Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                    startActivityForResult(intent, REQUEST_CODE_CHEAT);
                }
            });

         mNextButton = (ImageButton) findViewById(R.id.ButtonNext);
             mNextButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                     mIsCheater = mQuestionBank[mCurrentIndex].isUserCheat();

                     updateQuestion();
                     result();
                 }
         });
         mPreviousButton = (ImageButton) findViewById(R.id.ButtonPrevious);
             mPreviousButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                     if (mCurrentIndex < 0) {
                         mCurrentIndex = mQuestionBank.length - 1;
                     }

                     mIsCheater = mQuestionBank[mCurrentIndex].isUserCheat();

                     updateQuestion();
                     result();
                 }
             });
             updateQuestion();
             result();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

            if (mIsCheater) {
                mQuestionBank[mCurrentIndex].setUserCheat(true);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(RIGHT_ANSWERS, rightAnswers);
        savedInstanceState.putBoolean(TEST, mIsCheater);

        for (int i = 0; i < mQuestionBank.length; i++) {
            savedInstanceState.putBoolean(ANSWER_INDEX + i, mQuestionBank[i].isQuestionAnswered());
        }

        for (int i = 0; i < mQuestionBank.length; i++) {
            savedInstanceState.putBoolean(IS_CHEATER + i, mQuestionBank[i].isUserCheat());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}