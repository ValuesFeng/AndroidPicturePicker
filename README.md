# AndroidPicturePicker


##Thanks for :    
[Laevatein](https://github.com/nohana/Laevatein):Photo image selection activity set library. Currently under development.    
[Picasso](https://github.com/square/picasso):A powerful image downloading and caching library for Android    
[Glide](https://github.com/bumptech/glide):An image loading and caching library for Android focused on smooth scrolling    
[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader):Powerful and flexible library for loading, caching and displaying images on Android.  

##Screen Shot
![gallery](http://7sby47.com5.z0.glb.clouddn.com/screenshot1.jpg-xhdpi)
![gallery](http://7sby47.com5.z0.glb.clouddn.com/screenshot2.jpg-xhdpi)


## Usage    

Call photo image selection activity by the following code snipet.    
```java
public void onClickButton(View view) {
        Picker.from(this)
                .count(3)
                .enableCamera(true)
                .setEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
```    
You can use different Image Loader, Picker provide 3 Loader Engine:    
1.GlideEngine    
2.PicassoEngine    
3.ImageLoaderEngine    

or you can use custom engine , just like:    

    public static class CustomEngine implements LoadEngine {
        
       @Override
       public void displayImage(String path, ImageView imageView) {
        Log.i("picture", path);
        }
   
        @Override
        public void displayCameraItem(ImageView imageView) {

        }

        @Override
        public void scrolling(GridView view) {

        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        public CustomEngine() {

        }

        protected CustomEngine(Parcel in) {
        }

        public static final Creator<CustomEngine> CREATOR = new Creator<CustomEngine>() {
            public CustomEngine createFromParcel(Parcel source) {
                return new CustomEngine(source);
            }

            public CustomEngine[] newArray(int size) {
                return new CustomEngine[size];
            }
        };
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PicturePickerUtils.obtainResult(data);
            for (Uri u : mSelected) {
                Log.i("picture", u.getPath());
            }
        }
    }
     
     

Please note that LoadEngine is extends Parcelable.    

##Download
Gradle    
```gradle
   repositories {
    	maven { url "https://jitpack.io" }
   }  
  
  
  dependencies {
	   compile 'com.github.ValuesFeng:AndroidPicturePicker:1.0_alpha'
	}  
```

##License


    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


  