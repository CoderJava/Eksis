package com.ysn.eksis.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.ysn.eksis.BuildConfig
import com.ysn.eksis.R
import com.ysn.eksis.api.UnsplashApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DashboardActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    lateinit var accessToken: AccessToken
    lateinit var unsplashApi: UnsplashApi
    lateinit var menuBottomSheetDialogFragment: MenuBottomSheetDialogFragment
    var isOpenMenu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initEventBus()
        initFacebookService()
        initAccessToken()
        initMenuHamburger()
        initMenuBottomSheetDialogFragment()
        loadPhotoProfile()
        loadBackgroundProfile()
        loadProfile()
        loadFriends()
        loadPhotos()
    }

    private fun initEventBus() {
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onMessageEvent(hashMap: HashMap<String, Any>) {
        Log.d(TAG, "onMessageEvent")
        val animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(
            this@DashboardActivity,
            R.drawable.ic_menu_back_arrow_animatable
        )
        image_view_menu_activity_dashboard.setImageDrawable(animatedVectorDrawableCompat)
        animatedVectorDrawableCompat!!.start()
        isOpenMenu = false
    }

    private fun initMenuBottomSheetDialogFragment() {
        menuBottomSheetDialogFragment = MenuBottomSheetDialogFragment()
    }

    private fun initMenuHamburger() {
        val animatedVectorDrawable = resources.getDrawable(R.drawable.ic_menu_hamburger_animatable) as AnimatedVectorDrawable
        image_view_menu_activity_dashboard.setImageDrawable(animatedVectorDrawable)
        image_view_menu_activity_dashboard.setOnClickListener {
            val animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(
                    this@DashboardActivity,
                    if (isOpenMenu) R.drawable.ic_menu_back_arrow_animatable else R.drawable.ic_menu_hamburger_animatable
            )
            image_view_menu_activity_dashboard.setImageDrawable(animatedVectorDrawableCompat)
            animatedVectorDrawableCompat!!.start()
            if (isOpenMenu) {
                menuBottomSheetDialogFragment.dismiss()
            } else {
                menuBottomSheetDialogFragment.show(supportFragmentManager, TAG)
            }
            isOpenMenu = !isOpenMenu
        }
    }

    private fun initFacebookService() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val okhttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okhttpClient)
            .build()
        unsplashApi = retrofit.create(UnsplashApi::class.java)
    }

    private fun initAccessToken() {
        accessToken = AccessToken.getCurrentAccessToken()
    }

    @SuppressLint("CheckResult")
    private fun loadBackgroundProfile() {
        unsplashApi.getRandomImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val jsonObjectResponse = JSONObject(it.string())
                    val regularUrl = jsonObjectResponse.getJSONObject("urls").getString("small")
                    Glide.with(this@DashboardActivity)
                        .load(regularUrl)
                        .asBitmap()
                        .centerCrop()
                        .into(object : BitmapImageViewTarget(image_view_background_activity_dashboard) {
                            override fun setResource(resource: Bitmap?) {
                                Blurry.with(this@DashboardActivity)
                                    .from(resource)
                                    .into(image_view_background_activity_dashboard)
                            }
                        })
                },
                {
                    it.printStackTrace()
                },
                {
                    /* nothing to do in here */
                }
            )
    }

    private fun loadProfile() {
        val userId = accessToken.userId
        val parameters = Bundle()
        parameters.putString("fields", "name,gender,location")
        GraphRequest(
            accessToken,
            "/$userId",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback {
                val jsonObjectResponse = it.jsonObject
                text_view_name_activity_dashboard.text = jsonObjectResponse.getString("name")
                text_view_address_activity_dashboard.text = jsonObjectResponse.getJSONObject("location").getString("name")
            }
        ).executeAsync()
    }

    private fun loadFriends() {
        val userId = accessToken.userId
        GraphRequest(
                accessToken,
                "/$userId/friends",
                null,
                HttpMethod.GET,
                GraphRequest.Callback {
                    val totalCount = it.jsonObject.getJSONObject("summary").getInt("total_count")
                    text_view_value_friends.text = totalCount.toString()
                }
        ).executeAsync()
    }

    private fun loadPhotoProfile() {
        val userId = accessToken.userId
        Glide.with(this)
            .load("https://graph.facebook.com/v3.2/$userId/picture?type=large")
            .asBitmap()
            .centerCrop()
            .into(object : BitmapImageViewTarget(image_view_photo_profile_activity_dashboard) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmap = RoundedBitmapDrawableFactory.create(this@DashboardActivity.resources, resource)
                    circularBitmap.isCircular = true
                    image_view_photo_profile_activity_dashboard.setImageDrawable(circularBitmap)
                }
            })
    }

    private fun loadPhotos() {
        text_view_value_photos.text = "0"
    }


}
