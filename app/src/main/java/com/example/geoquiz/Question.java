package com.example.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mRightAnswer;

    public Question(int textResId, boolean rightAnswer) {
        mTextResId = textResId;
        mRightAnswer = rightAnswer;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isRightAnswer() {
        return mRightAnswer;
    }

    public void setRightAnswer(boolean rightAnswer) {
        mRightAnswer = rightAnswer;
    }

}


