package com.example.tomro.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mUserCorrect;
    private boolean mQuestionCompleted;



    public Question (int textResId, boolean answerTrue) {
        this.mAnswerTrue = answerTrue;
        this.mTextResId = textResId;
        mQuestionCompleted = false;
        mUserCorrect = false;
    }

    public int getTextResId() {
        return mTextResId;
    }
    public boolean getAnswerTrue() {
        return this.mAnswerTrue;
    }

    public void setTextResId(int Id) {
        this.mTextResId = Id;
    }

    public void setAnswerTrue(boolean isTrue) {

        this.mAnswerTrue = isTrue;
    }

    public void setUserCorrect(boolean isCorrect) {
        this.mUserCorrect = isCorrect;
    }

    public boolean getUserCorrect() {
        return this.mUserCorrect;
    }

    public void setQuestionCompleted() {
        this.mQuestionCompleted = true;
    }

    public boolean getQuestionCompleted() {
        return this.mQuestionCompleted;
    }


}
