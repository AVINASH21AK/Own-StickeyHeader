package com.apidemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    ArrayList<ArticlesModel> arrayArticle = new ArrayList<>();
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    Call callApiMethod;
    DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dbHelper = new DatabaseHelper(MainActivity.this);
        //dbHelper.dropAllTable();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        dbHelper.dropAllTable();

        callAPI();
    }

    private void callAPI() {

        try{

            if(App.isInternetAvail(MainActivity.this))
            {

                //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=03ac4499ca9b42e9a5ecea60cf2839e3

                callApiMethod = ApiClient.getRetrofitApiService().getList(
                        "the-next-web",
                        "latest",
                        "03ac4499ca9b42e9a5ecea60cf2839e3"
                );

                callApiMethod.enqueue(new Callback<DataList>() {
                    @Override
                    public void onResponse(Call<DataList> call, Response<DataList> response) {
                        try{

                            DataList datalist = response.body();
                            App.showLog(TAG, "====datalist====" + datalist);
                            App.showLog(TAG, "====datalist====" + response.body().toString());

                            if (datalist == null) {

                                App.showLogResponce("--null response--", "==Something wrong==");
                                ResponseBody responseBody = response.errorBody();

                                if (responseBody != null) {
                                    App.showLogResponce("------ Api Resonponce Body Null ----------", "" + responseBody.string());
                                }
                            } else {
                                App.showLog(TAG, "===Response==" + response.body().toString());
                                App.showLogResponce(    "Response Gson: ", new Gson().toJson(response.body()));


                                arrayArticle = datalist.articles;

                                for(int i=0; i<arrayArticle.size(); i++){
                                    dbHelper.dbInsertDate(arrayArticle.get(i));
                                }

                                setItemData(arrayArticle);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataList> call, Throwable t) {
                        t.printStackTrace();
                        App.showLog(TAG, "====onFailure====");
                    }
                });
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setItemData(ArrayList<ArticlesModel> arrayArticle) {

        dataAdapter = new DataAdapter(this, arrayArticle);
        recyclerView.setAdapter(dataAdapter);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(dataAdapter);
        recyclerView.addItemDecoration(headersDecor);

    }
    
    

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<DataAdapter.HeaderViewHolder>
    {
        private ArrayList<ArticlesModel> arrayArticle;
        private Context context;

        public DataAdapter(Context context, ArrayList<ArticlesModel> arrayItem) {
            this.context = context;
            this.arrayArticle = arrayItem;
        }


        //----- Child
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_list, viewGroup, false);
            return new ViewHolder(view);
        }


        @Override
        public int getItemCount() {
            return arrayArticle.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

            ArticlesModel articlesModel = arrayArticle.get(i);

            String author = articlesModel.author;
            String title = articlesModel.title;
            String description = articlesModel.description;
            String urltoimage = articlesModel.urlToImage;
            String publishedat = App.parseDateToddMMyyyy(articlesModel.publishedAt);

            viewHolder.tvAuthor.setText("Author: " + author);
            viewHolder.tvTitle.setText("Title: " + title);
            viewHolder.tvDescription.setText("Description: "+ description);
            viewHolder.tvPublishedAt.setText("PublishedAt: " + publishedat);

            Picasso.with(MainActivity.this)
                    .load(urltoimage)
                    .placeholder(R.drawable.tech)
                    .into(viewHolder.ivImage);

        }





        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvAuthor, tvTitle, tvDescription, tvPublishedAt;
            ImageView ivImage;

            public ViewHolder(View view) {
                super(view);

                tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvDescription = (TextView) view.findViewById(R.id.tvDescription);
                tvPublishedAt = (TextView) view.findViewById(R.id.tvPublishedAt);

                ivImage = (ImageView) view.findViewById(R.id.ivImage);

            }
        }


        //------  Header

        @Override
        public long getHeaderId(int position) {
            return position;
        }

        @Override
        public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_parent, parent, false);
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {

            ArticlesModel articlesModel = arrayArticle.get(position);

            String strDate1 = App.parseDateToddMMyyyy(arrayArticle.get(position).publishedAt);

            if(position>0)
            {
                String strDate2 = App.parseDateToddMMyyyy(arrayArticle.get(position-1).publishedAt);
                if(!strDate1.equals(strDate2))
                {
                    holder.tvHeader.setVisibility(View.VISIBLE);
                    holder.tvHeader.setText(""+App.parseDateToddMMyyyy(articlesModel.publishedAt));
                }
                else
                {
                    holder.tvHeader.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.tvHeader.setVisibility(View.VISIBLE);
                holder.tvHeader.setText(""+App.parseDateToddMMyyyy(articlesModel.publishedAt));
            }


        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            TextView tvHeader;

            public HeaderViewHolder(View view) {
                super(view);

                tvHeader = (TextView) view.findViewById(R.id.tvHeader);

            }
        }
    }
}
