/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nazmulidris.tinyurl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    UrlShortenService service;
    private TextView textOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponent();
        setupUI();
    }

    private void loadComponent() {
        DaggerUrlShortenServiceComponent
                .create()
                .injectObjectsIntoFieldsOf(this);
    }

    private void setupUI() {
        textOutput = findViewById(R.id.text_short_url);
        final Button button = findViewById(R.id.button_make_request);
        final EditText input = findViewById(R.id.edit_text_long_url);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(input.getText().toString());
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            log(MessageFormat.format("Running in background ... input = {0}",
                                     Arrays.asList(params).toString()));
            if (params.length > 0) {
                return service.getShortUrl(params[0]);
            } else {
                return "N/A";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            log("Done! ... Got result:" + response);
            textOutput.setText(response);
        }
    }

    public static final void log(String msg) {
        Log.i("tinyurl", msg);
    }

}
