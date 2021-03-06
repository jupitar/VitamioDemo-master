package utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import afasfafsafsdfad.R;
import bean.TestBean;

/**
 * Created by Administrator on 2017/2/13.
 */

public class BaseView {
    private LayoutInflater mInflater;

    private View view;
    private TestBean testBean;
    Context context ;
    TextView title,commentContents,test_right,test_your;
    RadioGroup radioGroup;
    LinearLayout comment_layout;
    RadioButton radioButton;
    public BaseView(){

    }





    public BaseView(Context context,TestBean testBean,boolean isCommentVisible){
        this.testBean=testBean;
        this.context= context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(isCommentVisible);
        initData(isCommentVisible);

    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    private void initView(boolean isCommentVisible) {
        view=mInflater.inflate(R.layout.test_item,null);
        title= (TextView) view.findViewById(R.id.title);

        radioGroup= (RadioGroup) view.findViewById(R.id.choicegroup);
        if(!isCommentVisible){
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                      String u_choice=radioButton.getText().toString();
                    testBean.setU_choice(u_choice.substring(0,1));
                    if(u_choice.substring(0,1).equals(testBean.getC_right()))
                        testBean.setIsTrue(1);
                    else
                        testBean.setIsTrue(0);
                }
            });

        }

        if(isCommentVisible){
            comment_layout=(LinearLayout)view.findViewById(R.id.comment_layout);
            commentContents=(TextView)view.findViewById(R.id.analize);
            test_right=(TextView)view.findViewById(R.id.test_right);
            test_your=(TextView)view.findViewById(R.id.test_your);
            comment_layout.setVisibility(View.VISIBLE);
            disableRadioGroup(radioGroup);

        }
        else{
            enableRadioGroup(radioGroup);

        }
    }


    public void initData(boolean isCommentVisible){
        title.setText("(单选题)"+" "+testBean.getTitle());
        if(isCommentVisible){
            commentContents.setText(testBean.getComments());
        }
        Map<String,Integer> map=new HashMap<String,Integer>();
        map.put("A",0);
        map.put("B",1);
        map.put("C",2);
        map.put("D",3);
        RadioButton radioButton= (RadioButton) radioGroup.getChildAt(0);
        radioButton.setText(testBean.getChoiceA());
        radioButton= (RadioButton) radioGroup.getChildAt(1);
        radioButton.setText(testBean.getChoiceB());
        radioButton= (RadioButton) radioGroup.getChildAt(2);
        radioButton.setText(testBean.getChoiceC());
        radioButton= (RadioButton) radioGroup.getChildAt(3);
        radioButton.setText(testBean.getChoiceD());
        if(isCommentVisible){//只有在查看错题时，试题才会有相关的颜色
            if(testBean.getIsTrue()==0){
                if(testBean.getU_choice()!=null){
                    int j=map.get(testBean.getU_choice());
                    test_your.setText(testBean.getU_choice());
                    radioButton= (RadioButton) radioGroup.getChildAt(j);
                    radioButton.setBackgroundColor(Color.RED);
                }else{
                    test_your.setText("（错误）");
                }
                int k=map.get(testBean.getC_right());

                radioButton= (RadioButton) radioGroup.getChildAt(k);
                radioButton.setBackgroundColor(Color.GREEN);
            }else{
                int  j=map.get(testBean.getC_right());
                radioButton= (RadioButton) radioGroup.getChildAt(j);
                radioButton.setBackgroundColor(Color.GREEN);
                test_your.setText(testBean.getU_choice());
                test_your.setTextColor(Color.GREEN);
            }
            test_right.setText(testBean.getC_right());
        }



    }

    //设置按钮不可点击
    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }
    //设置按钮可以点击

    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }
    /*private void selectRadioBtn(View v) {
        radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());

        String selectText = radioButton.getText().toString();


        Toast.makeText(context, selectText.substring(0,1), Toast.LENGTH_SHORT).show();

    }*/

}
