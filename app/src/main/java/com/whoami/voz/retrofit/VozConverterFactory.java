/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.whoami.voz.retrofit;

import com.whoami.voz.retrofit.converter.ForumConverter;
import com.whoami.voz.retrofit.converter.PostConverter;
import com.whoami.voz.retrofit.converter.ThreadConverter;
import com.whoami.voz.retrofit.data.ForumData;
import com.whoami.voz.retrofit.data.PostData;
import com.whoami.voz.retrofit.data.ThreadData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class VozConverterFactory extends Converter.Factory{
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (Document.class.equals(type)) {
            return new Converter<ResponseBody, Document>() {
                @Override public Document convert(ResponseBody value) throws IOException {
                    Document doc =  Jsoup.parse(value.string());
                    return doc;
                }
            };
        } else if (type.equals(PostData.class)) {
            return new PostConverter();
        } else if (type.equals(ForumData.class)) {
            return new ForumConverter();
        } else if (type.equals(ThreadData.class)) {
            return new ThreadConverter();
        }
        return null;
    }

//    @Override
//    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//        if (String.class.equals(type)) {
//            return new Converter<String, RequestBody>() {
//                @Override public RequestBody convert(String value) throws IOException {
//                    return RequestBody.create(MEDIA_TYPE, value);
//                }
//            };
//        }
//        return null;
//    }
}