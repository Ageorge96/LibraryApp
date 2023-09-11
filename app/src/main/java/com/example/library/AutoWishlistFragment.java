package com.example.library;

import static com.example.library.Operations.hideKeyboard;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AutoWishlistFragment extends Fragment {

    private RequestQueue requestQueue;
    private EditText searchEditText;

    private ImageButton searchBtn;

    private ListView resultListView;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initWidget(View view) {

        searchEditText = view.findViewById(R.id.input_addToWishAuto_autoSearch);
        searchBtn = view.findViewById(R.id.imagebtn_addToWishAuto_searchBook);

        progressBar = view.findViewById(R.id.progressbar_addToWishAuto_loadBooks);

        resultListView = view.findViewById(R.id.list_addToWishAuto_searchedBooks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_wishlist, container, false);
        initWidget(view);

        if (GoogleData.searchedBook != null) GoogleData.searchedBook = null;

        searchBtn.setOnClickListener(this::searchGoogleBooks);

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showBookDetails(position);
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    searchGoogleBooks(v);
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void searchGoogleBooks(View view) {

        hideKeyboard(getActivity());

        progressBar.setVisibility(View.VISIBLE);

        if (GoogleData.googleDataArrayList.size() != 0) {
            GoogleData.googleDataArrayList.removeAll(GoogleData.googleDataArrayList);
        } else {
            resultListView.setVisibility(View.INVISIBLE);
        }

        if (resultListView.getVisibility() == View.INVISIBLE) resultListView.setVisibility(View.VISIBLE);

        getGoogleBookList(searchEditText.getText().toString());
    }

    private void getGoogleBookList(String query) {

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest bookObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONArray bookArray = response.getJSONArray("items");

                    for (int i = 0; i < bookArray.length(); i++) {
                        JSONObject itemobj = bookArray.getJSONObject(i);
                        JSONObject volumeObj = itemobj.getJSONObject("volumeInfo");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        JSONObject saleInfo = itemobj.getJSONObject("saleInfo");



                        String title = volumeObj.optString("title");
                        String author = authorsArray.get(0).toString();
                        String pageCount = String.valueOf(volumeObj.optInt("pageCount"));
                        String description = volumeObj.optString("description");
                        String rating = volumeObj.optString("averageRating");


                        Log.i("2", "onResponse: " + title);
                        //Log.i("TAG", "onResponse: " + price);

                        String cover = null;

                        try {
                            String link = imageLinks.getString("thumbnail");
                            cover = link.substring(0, 4) + "s" + link.substring(4);
                        } catch (NullPointerException e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }


                        String price = null;

                        try {
                            JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                            price = "£" + listPrice.optString("amount");
                        } catch (JSONException e) {

                        }


                        GoogleData googleData = new GoogleData(title, author, pageCount, cover, rating, description, price);
                        GoogleData.googleDataArrayList.add(googleData);

                        setWishSearchAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Unable to find your book", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error found is " + error, Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(bookObjectRequest);
    }

    private void showBookDetails(int book) {

        GoogleData selectedBook = GoogleData.googleDataArrayList.get(book);

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_google_book_details);

        TextView titleTextView = dialog.findViewById(R.id.txt_adddialog_title);
        TextView authorTextView = dialog.findViewById(R.id.txt_adddialog_author);
        ImageView cover = dialog.findViewById(R.id.image_adddialog_cover);

        Button yesBtn = dialog.findViewById(R.id.btn_adddialog_yes);
        Button noBtn = dialog.findViewById(R.id.btn_adddialog_no);

        titleTextView.setText(selectedBook.getGoogleTitle());
        authorTextView.setText(selectedBook.getGoogleAuthor());

        new DownloadURLImage((ImageView) cover).execute(selectedBook.getCover());

        dialog.show();


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleData.searchedBook = selectedBook;
                dialog.dismiss();

                CheckBox autoManualCheckBox = getActivity().findViewById(R.id.checkbox_addToWish_autoManual);

                autoManualCheckBox.toggle();
                autoManualCheckBox.setText(R.string.to_auto);

                getActivity().finish();

                startActivity(getActivity().getIntent());
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void setWishSearchAdapter() {
        GoogleSearchAdapter googleSearchAdapter = new GoogleSearchAdapter(getActivity().getApplicationContext(), GoogleData.googleDataArrayList);
        resultListView.setAdapter(googleSearchAdapter);
    }


}