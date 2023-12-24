package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_IntroView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class ActivityGetStared extends AppCompatActivity {

    private ViewPager2 viewPager2_getstarted;
    private CircleIndicator3 CircleIndicator3_GetStrated;
    Button btnNext_custom_get_stared_item, btnBoQua_custom_get_stared_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stared);
        Init();
        int[] dsHinh={R.drawable.intro2,R.drawable.intro1, R.drawable.intro3};
        ArrayList<String> dsTitle=new ArrayList<String>();
        dsTitle.add("Chào mừng đến với ứng dụng du lịch Việt Nam!");
        dsTitle.add("Bạn đang tìm những điểm du lich thú vị?");
        dsTitle.add("Khám phá tiện ích chia sẻ khách sạn.");

        ArrayList<String> dsDetail=new ArrayList<String>();
        dsDetail.add("Ứng dụng sẽ đưa bạn đến mọi điểm du lịch khắp Việt Nam.Ứng dụng này cung cấp thông"
                +" tin đáng tin cậy về các điểm đến du lịch, thông tin các khách sạn khu nghĩ dưỡng cần thiết cho chuyến du"
                +" lịch của bạn, giúp bạn có một hành trình du lịch đáng nhớ. ");
        dsDetail.add("Hãy đến với ứng dụng, nơi mà bạn có thể tìm những thông tin du lịch hữu ích. Đồng thời, tại đây bạn cũng có"
                +"thể chia sẻ những trãi nghiệm du lịch của bạn đến với mọi người");
        dsDetail.add("Tận hưởng trải nghiệm du lịch đáng nhớ cùng bạn bè và gia đình với chức năng tìm và chia sẻ khách sạn."
                +" Với chức năng chia sẻ khách sạn nó sẽ giúp bạn tìm những khách sạn, khu nghĩ dưỡng gần nhất");

        Adapter_IntroView adapter_introView=new Adapter_IntroView(dsTitle,dsDetail,dsHinh,
                ActivityGetStared.this);
        viewPager2_getstarted.setAdapter(adapter_introView);

        CircleIndicator3_GetStrated.setViewPager(viewPager2_getstarted);

        btnNext_custom_get_stared_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexItem=viewPager2_getstarted.getCurrentItem()+1;
                if(indexItem==dsTitle.size()-1)
                {
                    btnBoQua_custom_get_stared_item.setVisibility(View.INVISIBLE);
                    btnNext_custom_get_stared_item.setText("Đăng ký");
                    viewPager2_getstarted.setCurrentItem(viewPager2_getstarted.getCurrentItem()+1);
                }
                else if( indexItem>dsTitle.size()-1)
                {
                    Intent intent =new Intent(ActivityGetStared.this, ActivityRegister.class);
                    ActivityGetStared.this.startActivity(intent);
                }
                else
                {
                    btnBoQua_custom_get_stared_item.setVisibility(View.VISIBLE);
                    btnNext_custom_get_stared_item.setText("Đăng ký");
                    viewPager2_getstarted.setCurrentItem(viewPager2_getstarted.getCurrentItem()+1);
                }

            }
        });
        btnBoQua_custom_get_stared_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2_getstarted.setCurrentItem(dsTitle.size()-1);
            }
        });
    }
    private void Init()
    {
        viewPager2_getstarted=findViewById(R.id.viewPager2_getstarted);
        CircleIndicator3_GetStrated=findViewById(R.id.CircleIndicator3_GetStrated);
        btnNext_custom_get_stared_item=findViewById(R.id.btnNext_custom_get_stared_item);
        btnBoQua_custom_get_stared_item=findViewById(R.id.btnBoQua_custom_get_stared_item);

    }
}