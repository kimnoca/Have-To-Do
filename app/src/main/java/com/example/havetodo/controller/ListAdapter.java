package com.example.havetodo.controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.havetodo.R;
import com.example.havetodo.model.TODO;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder>
        implements ItemTouchHelperListener, OnDialogListener {

    ArrayList<TODO> items = new ArrayList<>();
    Context context;

    public ListAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해서 원하는 레이아웃을 띄워줌
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //ItemViewHolder가 생성되고 넣어야할 코드들을 넣어준다.
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(TODO todo) {
        items.add(todo);
    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        TODO todo = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position);
        //이동하고 싶은 position에 추가
        items.add(to_position, todo);

        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    //왼쪽 버튼 누르면 수정할 다이얼로그 띄우기
    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        //수정 버튼 클릭시 다이얼로그 생성
        CustomDialog dialog = new CustomDialog(context, position, items.get(position));

        //화면 사이즈 구하기
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //다이얼로그 사이즈 세팅
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();
        wm.copyFrom(dialog.getWindow().getAttributes());
        wm.width = (int) (width * 0.7);
        wm.height = height / 2;

        //다이얼로그 Listener 세팅
        dialog.setDialogListener(this);

        //다이얼로그 띄우기
        dialog.show();
    }


    //오른쪽 버튼 누르면 아이템 삭제
    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public void onFinish(TODO todo, int position) {
        items.set(position, todo);
        notifyItemChanged(position);
    }



    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView list_title;
        TextView list_content;
        TextView list_date;


        public ItemViewHolder(View itemView) {
            super(itemView);
            list_title = itemView.findViewById(R.id.list_title);
            list_content = itemView.findViewById(R.id.list_content);
            list_date = itemView.findViewById(R.id.list_date);
        }

        public void onBind(TODO todo) {
            list_title.setText(todo.getTodoTitle());
            list_content.setText(todo.getTodoContent());
            list_date.setText(String.valueOf(todo.getAtDate()));
        }
    }


}