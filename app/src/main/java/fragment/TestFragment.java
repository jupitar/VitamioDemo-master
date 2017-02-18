package fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import activity.ErrorTestActivity;
import activity.MainActivity;
import activity.R;
import activity.TestActivity;
import base.DBManager;
import bean.TestBean;
import bean.TestSummary;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static utils.URLUtils.INSERT_EXAMSERVLET;
import static utils.URLUtils.UBASIC_EXAMSERVLET;


/**
 * Created by Administrator on 2017/1/8.
 */
//在线测试fragment

public class TestFragment extends Fragment {
    TextView total_number,finished_number,right_percent;
    Button modern_test,look_error,insert_data;
    String userName="";
    DBManager dbManager;
    MainActivity activity;

    public TestFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View v=  LayoutInflater.from(getActivity()).inflate(R.layout.test_fragment, null, false);

        //获取数据总记录数目
       // getCount();
        init(v);

        return v;
    }

    private int getCount() {
        dbManager=new DBManager(getActivity());
        return dbManager.getCount();
       //return  dbManager.getOffsetCount(0,5,0);

    }

    private void init(View v) {
        userName=((MainActivity)getActivity()).getName();
        total_number=(TextView) v.findViewById(R.id.total_number);
      //  total_number.setText("试题总数目为:"+getCount());

        finished_number=(TextView) v.findViewById(R.id.finish_number);
        right_percent=(TextView) v.findViewById(R.id.right_percent);

        modern_test= (Button) v.findViewById(R.id.modern_test);
        look_error= (Button) v.findViewById(R.id.look_error);
        insert_data=(Button) v.findViewById(R.id.sert_into);
        setClick();

        getDatas(userName);

    }

    private void getDatas(String userName) {
        postConnection(UBASIC_EXAMSERVLET, userName);
    }

    private void setClick() {
        activity=(MainActivity)getActivity();


        //模块测试

        modern_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =new Intent(activity, TestActivity.class);
               intent.putExtra("userName",userName);
                startActivity(intent);

            }
        });

        //查看错题
        look_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), ErrorTestActivity.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
        //插入数据

        insert_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postConnection( INSERT_EXAMSERVLET,userName);
              /*  Intent intent =new Intent(getActivity(), ErrorTestActivity.class);

                intent.putExtra("userName",userName);
                startActivity(intent);*/
            }
        });




    }
    //网络请求数据

    public  void postConnection(String url,String userName) {//post提交数据请求
        OkHttpClient mOkHttpClient = new OkHttpClient();
        TestBean testBeans=new TestBean(30,"wp2","哈哈哈哈哈哈!","B哈哈哈哈哈哈!","B",1,"10:45");

        RequestBody requestBodyPost = new FormBody.Builder().add("userName", userName).add("testBeans",new Gson().toJson(testBeans)).build();
        Request requestPost = new Request.Builder().url(url).post(requestBodyPost).build();
        mOkHttpClient.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.equals("success")){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"插入数据成功!",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return ;


                }
                if(result.equals("fail")){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"插入数据失败!",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return ;


                }
                Gson gson=new Gson();
              final  TestSummary testSummary=gson.fromJson(result,TestSummary.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        total_number.setText("总共有"+testSummary.getCount()+"道题");
                        if(testSummary.getHasFinished()==0){

                            right_percent.setText("");
                        }else{
                            java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
                            right_percent.setText("正确率为:"+df.format(testSummary.getPercent()*100)+"%");
                        }
                        finished_number.setText("您已经完成了"+testSummary.getHasFinished()+"道题");


                    }
                });


            }
        });


    }

}
