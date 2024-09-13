package com.example.yrsllp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import JavaClass.Category;
import JavaClass.StructureClass;

public class DictionaryCategoryAdapter extends RecyclerView.Adapter<DictionaryCategoryAdapter.ViewHolder>{

    interface OnCategoryClickListener{
        void onCategoryClick(Category category, int position);
    }

    private final OnCategoryClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Category> categories;
    private Context mContext;
    private int[] colors = {R.color.category_first, R.color.category_second, R.color.category_third, R.color.category_fourth, R.color.category_fifth};

    DictionaryCategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener onClickListener) {
        this.mContext = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public DictionaryCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dictionary_category_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DictionaryCategoryAdapter.ViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        //Выбор цвета заднего фона
        int color = colors[position % colors.length];
//        holder.cardView.setCardBackgroundColor(color);
        holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(colors[position % colors.length]));


        //Заполнение внутренностей
        Category category = categories.get(position);
        Glide.with(mContext).load(category.getImageURL()).into(holder.categoryImage);
        holder.categoryName.setText(category.getTitle());
        holder.categoryDescription.setText(category.getDescription());


        int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onCategoryClick(category, finalPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView categoryImage;
        final TextView categoryName;
        final TextView categoryDescription;
        final CardView cardView;
        ViewHolder(View view){
            super(view);
            categoryImage = view.findViewById(R.id.dictionary_category_list_img);
            categoryName = view.findViewById(R.id.dictionary_category_list_title);
            categoryDescription = view.findViewById(R.id.dictionary_category_list_description);
            cardView = view.findViewById(R.id.dictionary_category_card_view);
        }
    }
}