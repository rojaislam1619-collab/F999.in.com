package com.example.data.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object CasinoChatbotService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun getBotResponse(userMessage: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val geminiResponse = callGeminiApi(userMessage, apiKey)
                if (!geminiResponse.isNullOrBlank()) {
                    return@withContext geminiResponse
                }
            } catch (e: Exception) {
                // Fallback to local intelligent rule-based responses
            }
        }

        // Local Smart AI Fallback in Bengali and English
        generateSmartLocalResponse(userMessage)
    }

    private fun callGeminiApi(prompt: String, apiKey: String): String? {
        val systemPrompt = "You are F999 AI Casino & Payment Concierge (গ্রাহক সহায়তা বট). You assist users in Bengali and English. F999 features 24/7 automated instant withdrawals via bKash, Nagad, Rocket, USDT in 30-60s, AES-256 encryption, Provably Fair games (Slots, Crash X, Lucky Wheel), and an active Admin Audit panel. Be polite, concise, helpful, and professional."

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
        val jsonPayload = JSONObject().apply {
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
            })
            put("contents", JSONArray().put(JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().put("text", prompt)))
            }))
        }

        val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: return null
        if (!response.isSuccessful) return null

        val root = JSONObject(responseBody)
        val candidates = root.optJSONArray("candidates") ?: return null
        val firstCandidate = candidates.optJSONObject(0) ?: return null
        val content = firstCandidate.optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        val text = parts.optJSONObject(0)?.optString("text")
        return text
    }

    private fun generateSmartLocalResponse(input: String): String {
        val lower = input.lowercase().trim()

        return when {
            lower.contains("উইথড্রল") || lower.contains("withdraw") || lower.contains("টাকা তোলা") -> {
                "⚡ **দ্রুত উইথড্রল সহায়তা (Fast Automated Withdrawal):**\n" +
                "• F999-এ উন্নত অটোমেটেড সিস্টেমের মাধ্যমে উইথড্রল মাত্র ৩০ থেকে ৬০ সেকেন্ডে স্বয়ংক্রিয়ভাবে প্রসেস হয়।\n" +
                "• আপনি বিকাশ, নগদ, রকেট, বা USDT TRC20 এর মাধ্যমে সর্বনিম্ন ৳৫০০ থেকে সর্বোচ্চ ৳৫,০০,০০০ পর্যন্ত উইথড্র করতে পারেন।\n" +
                "• 'উইথড্রল' ট্যাবে গিয়ে আপনার একাউন্ট নম্বর ও টাকার পরিমাণ দিন; আমাদের AI রিস্ক ইঞ্জিন সাথে সাথে ব্যাংক/MFS এ ফান্ড ট্রান্সফার করবে।"
            }

            lower.contains("ডিপোজিট") || lower.contains("deposit") || lower.contains("টাকা জমা") || lower.contains("payment") -> {
                "💳 **নিরাপদ পেমেন্ট গেটওয়ে (Secure Deposit):**\n" +
                "• বিকাশ (bKash), নগদ (Nagad), রকেট (Rocket), এবং USDT (TRC20) সমর্থিত।\n" +
                "• ডিপোজিট অপশনে গিয়ে যেকোনো গেটওয়ে সিলেক্ট করুন, প্রদর্শিত নাম্বারে 'Send Money' করুন এবং ১০ অক্ষরের TrxID ইনপুট দিয়ে সাবমিট করুন।\n" +
                "• আপনার ওয়ালেটে তাত্ক্ষণিকভাবে টাকা যোগ হবে এবং ট্রানজেকশনটি AES-256 এনক্রিপশনের মাধ্যমে সুরক্ষিত থাকবে।"
            }

            lower.contains("নিরাপত্তা") || lower.contains("security") || lower.contains("audit") || lower.contains("অডিট") || lower.contains("এনক্রিপশন") -> {
                "🛡️ **ডেটা এনক্রিপশন ও সিকিউরিটি অডিট:**\n" +
                "• F999-এর প্রতিটি লেনদেন এবং ইউজার ডেটা **AES-256-GCM** মিলিটারি গ্রেড এনক্রিপশনে সুরক্ষিত।\n" +
                "• আমাদের সিস্টেম নিয়মিত স্বয়ংক্রিয় সিকিউরিটি অডিট পরিচালনা করে (TLS 1.3, DDoS Mitigation & Database at Rest Encryption)।\n" +
                "• 'সিকিউরিটি' মেন্যু থেকে আপনি নিজেই লাইভ সিকিউরিটি অডিট চালাতে ও Provably Fair ক্রিপ্টোগ্রাফিক হ্যাশ যাচাই করতে পারেন।"
            }

            lower.contains("গেম") || lower.contains("game") || lower.contains("slot") || lower.contains("crash") || lower.contains("রুলস") -> {
                "🎰 **জনপ্রিয় গেম ও খেলার নিয়ম:**\n" +
                "1. **Lucky 777 Slots:** স্পিন করুন এবং ৭-৭-৭ মিলিয়ে বিশাল জ্যাকপট জিতুন (RTP 97%)!\n" +
                "2. **Rocket Crash X:** রকেট উড়ার সাথে সাথে মাল্টিপ্লায়ার বাড়বে (1.0x থেকে 50x+)। ক্র্যাশ হওয়ার আগেই 'Cash Out' করে টাকা তুলে নিন।\n" +
                "3. **Fortune Wheel:** চাকা ঘুরিয়ে ইনস্ট্যান্ট মাল্টিপ্লায়ার বা বোনাস চিপস পান।\n" +
                "4. **Dragon Tiger:** টেবিল কার্ড গেমে ড্রাগন বা টাইগারের পক্ষে বাজি ধরে দ্বিগুণ জিতুন।"
            }

            lower.contains("এডমিন") || lower.contains("admin") -> {
                "👑 **এডমিন প্যানেল এক্সেস:**\n" +
                "• উপরে ডানদিকের 'Admin' আইকন অথবা সেটিংসে গিয়ে এডমিন ড্যাশবোর্ড দেখতে পারবেন।\n" +
                "• এডমিন প্যানেল থেকে সমস্ত অটোমেটেড উইথড্রল অনুমোদন, পেমেন্ট গেটওয়ে কনফিগারেশন, ইউজার ব্যালেন্স ও সিকিউরিটি অডিট লগ পর্যবেক্ষণ করা যায়।"
            }

            lower.contains("bonuses") || lower.contains("bonus") || lower.contains("বোনাস") || lower.contains("vip") -> {
                "🎁 **VIP রিওয়ার্ডস ও ক্যাশব্যাক:**\n" +
                "• প্রথম ডিপোজিটে ১০০% ওয়েলকাম বোনাস!\n" +
                "• VIP লেভেল বৃদ্ধির সাথে সাথে ০.৫% থেকে ২.০% পর্যন্ত দৈনিক রিবেট এবং বিশেষ উইথড্রল সুবিধা পাওয়া যায়।"
            }

            lower.contains("হ্যালো") || lower.contains("hi") || lower.contains("hello") || lower.contains("কেমন") -> {
                "নমস্কার! F999 গ্রাহক সহায়তা সেন্টারে আপনাকে স্বাগতম। আমি আপনার এআই অ্যাসিস্ট্যান্ট।\n" +
                "ডিপোজিট, ফাস্ট উইথড্রল, গেমের নিয়ম বা সিস্টেম সিকিউরিটি সম্পর্কে আপনার যেকোনো প্রশ্ন করতে পারেন!"
            }

            else -> {
                "ধন্যবাদ আপনার বার্তার জন্য। F999 গ্রাহক সহায়তা বট সার্বক্ষণিক আপনার পাশে রয়েছে।\n" +
                "আপনার ডিপোজিট, উইথড্রল স্ট্যাটাস, গেমের নিয়ম বা সিকিউরিটি অডিট সংক্রান্ত যেকোনো তথ্য জানতে নিচের বাটনগুলোতে ক্লিক করতে পারেন অথবা বিস্তারিত লিখুন।"
            }
        }
    }
}
