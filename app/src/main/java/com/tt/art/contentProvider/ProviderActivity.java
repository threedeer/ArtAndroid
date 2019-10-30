package com.tt.art.contentProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tt.art.R;
import com.tt.art.base.BaseActivity;
import com.tt.art.databinding.ActivityProviderBinding;

/**
 * @author T
 * @date 2019/10/11
 * @Description
 */
public class ProviderActivity extends BaseActivity<ActivityProviderBinding> {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        //唯一标识provider
//        Uri uri = Uri.parse("content://com.tt.art.contentProvider");
//        getContentResolver().query(uri, null, null, null, null);
//        getContentResolver().query(uri, null, null, null, null);
//        getContentResolver().query(uri, null, null, null, null);

        Uri bookUri = Uri.parse("content://com.tt.art.contentProvider.BookProvider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "程序设计的艺术");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null,null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.d("-----", "query book:" + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.tt.art.contentProvider.BookProvider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name","sex"}, null, null,null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.userId = userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.isMale = userCursor.getInt(2) == 1;
            Log.d("-----", "query user:" + user.toString());
        }
        userCursor.close();


    }

    private class Book {
        public int bookId;
        public String bookName;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "bookId=" + bookId +
                    ", bookName='" + bookName + '\'' +
                    '}';
        }
    }

    private class User {
        public int userId;
        public String userName;
        public boolean isMale;

        public boolean isMale() {
            return isMale;
        }

        public void setMale(boolean male) {
            isMale = male;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", isMale=" + isMale +
                    '}';
        }
    }
}
