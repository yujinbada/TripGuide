//package com.example.tripguide.fragment.recommend
//
//import android.os.AsyncTask
//import android.util.Log
//import com.example.tripguide.model.RecommendAreaCode
//import com.example.tripguide.utils.Constants
//import com.example.tripguide.utils.Constants.TAG
//import com.google.api.AnnotationsProto.http
//import org.xmlpull.v1.XmlPullParser
//import org.xmlpull.v1.XmlPullParserFactory
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.io.StringReader
//import java.net.URL
//
//    private val arrayList = ArrayList<RecommendAreaCode>()
//    private val areaNameMap = mutableMapOf<String, String>()
//
//    val mobile_os = "AND"
//    val mobile_app = "TripGuide"
//    val serviceUrl = "http://apis.data.go.kr/B551011/KorService/areaCode"
//    val serviceKey = "LUjHE2JtNIM0j7H1yjIJnSkVhIS6p6I6R0y5F235iEiBQL9it8MXwm6mjNUFYGbnDpVFsqLgeYnIqcMNF83ilg%3D%3D"
//
//fun findAreaCode(areaname : String): ArrayList<RecommendAreaCode> {
//    Log.d(TAG, "FindAreaCode() called")
//    val requstUrl = serviceUrl +
//            "?serviceKey=" + serviceKey +
//            "&numOfRows=20" +
//            "&MobileApp=" + mobile_app +
//            "&MobileOS=" + mobile_os
//
//    fetchXML(requstUrl, areaname)
//
//    return arrayList
//}
//
//private fun fetchXML(url: String, areaname: String) {
//    lateinit var page : String  // url 주소 통해 전달받은 내용 저장할 변수
//
//    // xml 데이터 가져와서 파싱하기
//    // 외부에서 데이터 가져올 때 화면 계속 동작하도록 AsyncTask 이용
//    class getDangerGrade : AsyncTask<Void, Void, Void>() {
//        // url 이용해서 xml 읽어오기
//        override fun doInBackground(vararg p0: Void?): Void? {
//            // 데이터 스트림 형태로 가져오기
//            val stream = URL(url).openStream()
//            val bufReader = BufferedReader(InputStreamReader(stream, "UTF-8"))
//
//            // 한줄씩 읽어서 스트링 형태로 바꾼 후 page에 저장
//            page = ""
//            var line = bufReader.readLine()
//            while (line != null) {
//                page += line
//                line = bufReader.readLine()
//            }
//
//            return null
//        }
//
//        // 읽어온 xml 파싱하기
//        override fun onPostExecute(result: Void?) {
//            super.onPostExecute(result)
//
//            var tagCode = false    // code tag
//            var tagAreaName = false    // name tag
//
//            var code = ""          // code
//            var areaname = ""          // name
//
//
//            var factory = XmlPullParserFactory.newInstance()    // 파서 생성
//            factory.setNamespaceAware(true)                     // 파서 설정
//            var xpp = factory.newPullParser()                   // XML 파서
//
//            // 파싱하기
//            xpp.setInput(StringReader(page))
//
//            // 파싱 진행
//            var eventType = xpp.eventType
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                if (eventType == XmlPullParser.START_DOCUMENT) {}
//                else if (eventType == XmlPullParser.START_TAG) {
//                    var tagName = xpp.name
//                    if (tagName.equals("code")) tagCode = true
//                    else if (tagName.equals("name")) tagAreaName = true
//
//                }
//                if (eventType == XmlPullParser.TEXT) {
//                    if (tagAreaName) {
//                        areaname = xpp.text
//                        tagAreaName = false
//                        // 기관명까지 다 읽으면 하나의 데이터 다 읽은 것임
//                        areaNameMap[areaname] = code
//
//                        Log.d(TAG, "areaNameMap - $areaNameMap")
//                        val areaNameSplit = areaname.split(" ")
//                        if(areaNameSplit.size == 1) {
//
//                            Log.d(TAG, "areaNameMap - $areaNameMap")
//                            val areaCode = areaNameMap.filter { it.key == areaname }.values.toString()
//                            arrayList.add(RecommendAreaCode(areaCode))
//                        }
//                        else {
//                            val areaCode = areaNameMap.filter { it.key == areaNameSplit[0] }.values.toString()
//                            areaNameMap.clear()
//
//                            val requstUrl2 = serviceUrl +
//                                    "?serviceKey=" + serviceKey +
//                                    "&areaCode=" + areaCode +
//                                    "&numOfRows=20" +
//                                    "&MobileApp=" + mobile_app +
//                                    "&MobileOS=" + mobile_os
//
//                            val sigunguCode = areaNameMap.filter { it.key == areaNameSplit[1] }.values.toString()
//                            arrayList.add(RecommendAreaCode(areaCode, sigunguCode))
//                        }
//
//                    }
//                    else if (tagCode) {
//                        code = xpp.text
//                        tagCode = false
//                    }
//                }
//                if (eventType == XmlPullParser.END_TAG) {}
//                eventType = xpp.next()
//            }
//        }
//    }
//
//    getDangerGrade().execute()
//}