package com.example.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mRightAnswer;
    private boolean mIsQuestionAnswered;

    public boolean isQuestionAnswered() {
        return mIsQuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        mIsQuestionAnswered = questionAnswered;
    }

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


