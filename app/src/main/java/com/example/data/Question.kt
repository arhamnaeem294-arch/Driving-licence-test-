package com.example.data

enum class AppLanguage(val code: String, val titleUrdu: String, val titleEnglish: String, val flagEmoji: String) {
    URDU("ur", "اردو", "Urdu", "🇵🇰"),
    ENGLISH("en", "English", "English", "🇬🇧"),
    ROMAN("roman", "رومن اردو", "Roman Urdu", "🔤")
}

enum class QuestionCategory(
    val titleUrdu: String,
    val titleEnglish: String,
    val titleRoman: String,
    val partNameUrdu: String,
    val partNameEnglish: String
) {
    MANDATORY("لازمی نشانات", "Mandatory Signs", "Lazmi Nishan", "حصہ 1", "Part 1"),
    WARNING("انتباہی نشانات", "Warning Signs", "Warning Nishan", "حصہ 2", "Part 2"),
    GENERAL_KNOWLEDGE("جنرل نالج و قوانین", "General Knowledge", "General Knowledge", "حصہ 3", "Part 3");

    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> titleUrdu
        AppLanguage.ENGLISH -> titleEnglish
        AppLanguage.ROMAN -> titleRoman
    }

    fun getPartName(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> partNameUrdu
        AppLanguage.ENGLISH -> partNameEnglish
        AppLanguage.ROMAN -> partNameUrdu
    }
}

enum class VehicleType(
    val titleUrdu: String,
    val titleEnglish: String,
    val titleRoman: String,
    val emoji: String
) {
    ALL("تمام گاڑیاں", "All Vehicles", "All Vehicles", "🇵🇰"),
    MOTORCYCLE("موٹر سائیکل / سکوٹر", "Motorcycle / Bike", "Motorcycle / Bike", "🏍️"),
    CAR_LTV("کار / ایل ٹی وی", "Car / LTV", "Car / LTV", "🚘"),
    TRUCK_HTV("ایچ ٹی وی / ٹرک / بس", "HTV / Heavy Truck", "HTV / Heavy Truck", "🚚"),
    COMMERCIAL_BUILDER("کمرشل / ٹریکٹر / کرین", "Tractor / Commercial", "Tractor / Commercial", "🚜")
}

enum class SignVisualType {
    STOP_OCTAGON,
    NO_ENTRY,
    BLUE_CIRCLE_RIGHT,
    BLUE_CIRCLE_LEFT,
    WARNING_TRIANGLE,
    NO_HORN,
    SPEED_LIMIT_50,
    GO_STRAIGHT,
    NO_PEDESTRIAN,
    NO_CYCLE,
    NO_UTURN,
    NO_OVERTAKING,
    PARKING_ALLOWED,
    NO_PARKING,
    NO_STOPPING,
    WEIGHT_LIMIT,
    HEIGHT_LIMIT,
    NO_TRUCKS,
    ROUNDABOUT,
    END_RESTRICTION,
    SLIPPERY_ROAD,
    SCHOOL_AHEAD,
    WORK_IN_PROGRESS,
    SHARP_TURN,
    CROSS_ROAD,
    ZIGZAG_ROAD,
    CHILDREN_CROSSING,
    ROAD_CLOSED,
    RAILWAY_CROSSING,
    STEEP_DESCENT,
    STEEP_ASCENT,
    NARROW_BRIDGE,
    ANIMALS_CROSSING,
    LOW_AIRCRAFT,
    FALLING_ROCKS,
    TRAFFIC_LIGHT,
    SEATBELT,
    HEADLIGHT,
    NO_DRUNK,
    NO_PHONE,
    HELMET,
    OVERTAKING_RULE,
    AMBULANCE,
    ZEBRA_CROSSING,
    FOG_DRIVING,
    PRE_TRIP_CHECK,
    YELLOW_LIGHT,
    SIDE_MIRRORS,
    SPEEDING_FINE,
    LICENSE_RULE,
    BIKE_PILLION,
    BIKE_BRAKING,
    TRUCK_BLINDSPOT,
    TRUCK_BRAKES,
    TRACTOR_LIGHTS,
    BUILDER_EQUIPMENT
}

data class Question(
    val id: Int,
    val category: QuestionCategory,
    val vehicleType: VehicleType = VehicleType.ALL,
    val questionUrdu: String,
    val questionRoman: String,
    val questionEnglish: String = "",
    val emoji: String,
    val signType: SignVisualType,
    val options: List<String>,
    val optionsEnglish: List<String> = emptyList(),
    val correctOptionIndex: Int,
    val explanation: String,
    val explanationEnglish: String = ""
) {
    fun getQuestionText(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> questionUrdu
        AppLanguage.ENGLISH -> if (questionEnglish.isNotBlank()) questionEnglish else questionRoman
        AppLanguage.ROMAN -> questionRoman
    }

    fun getOptionsText(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.URDU -> options
        AppLanguage.ENGLISH -> if (optionsEnglish.isNotEmpty()) {
            optionsEnglish
        } else {
            options.map { opt ->
                val regex = Regex("\\(([^)]+)\\)")
                val match = regex.find(opt)
                match?.groupValues?.get(1) ?: opt
            }
        }
        AppLanguage.ROMAN -> options
    }

    fun getExplanationText(lang: AppLanguage): String = when (lang) {
        AppLanguage.URDU -> explanation
        AppLanguage.ENGLISH -> if (explanationEnglish.isNotBlank()) explanationEnglish else explanation
        AppLanguage.ROMAN -> explanation
    }
}

object QuestionsRepository {
    val questions: List<Question> = listOf(
        // ==========================================
        // 🏍️ MOTORCYCLE / BIKE SPECIFIC QUESTIONS
        // ==========================================
        Question(
            id = 1,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🏍️ موٹر سائیکل چلاتے وقت ہیلمٹ کا استعمال کس کے لیے ضروری ہے؟",
            questionRoman = "Motorcycle chalate waqt helmet kis ke liye zaroori hai?",
            emoji = "🪖",
            signType = SignVisualType.HELMET,
            options = listOf("ڈرائیور اور پچھلی سواری دونوں کے لیے (Both Rider & Pillion)", "صرف ڈرائیور کے لیے (Driver Only)", "صرف بچوں کے لیے (Kids Only)", "کسی کے لیے نہیں (None)"),
            correctOptionIndex = 0,
            explanation = "پاکستان کے ٹریفک قوانین کے مطابق موٹر سائیکل ڈرائیور اور پیچھے بیٹھی سواری دونوں کے لیے ہیلمٹ پہننا لازمی ہے۔"
        ),
        Question(
            id = 2,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🏍️ موٹر سائیکل پر ایک وقت میں کتنے افراد سوار ہو سکتے ہیں؟",
            questionRoman = "Motorcycle par kitne log sawar ho sakte hain?",
            emoji = "🏍️",
            signType = SignVisualType.BIKE_PILLION,
            options = listOf("زیادہ سے زیادہ 2 افراد (Max 2 Persons)", "3 افراد (3 Persons)", "4 افراد (4 Persons)", "کوئی حد نہیں (No Limit)"),
            correctOptionIndex = 0,
            explanation = "ون ڈشنگ یا دو سے زائد افراد کی سواری (Double Pillion) قانوناً ممنوع اور خطرناک ہے۔"
        ),
        Question(
            id = 3,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🌧️ بارش میں موٹر سائیکل کے بریکس گیلے ہو جائیں تو کیا کرنا چاہیے؟",
            questionRoman = "Barish me brakes geele ho jayein to?",
            emoji = "🌧️",
            signType = SignVisualType.BIKE_BRAKING,
            options = listOf("آہستہ آہستہ بریک دبا کر ڈرائی کریں (Apply light brake to dry)", "تیز رفتاری سے چلائیں (Speed up)", "فورا فل بریک لگائیں (Hard Brake)", "نیوٹرل کر دیں (Neutral)"),
            correctOptionIndex = 0,
            explanation = "گیلے بریکس کو خشک کرنے کے لیے ہلکی بریک کا استعمال کریں تاکہ رگڑ سے پانی صاف ہو سکے۔"
        ),
        Question(
            id = 4,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🏍️ موٹر سائیکل کو سڑک کی کس لین میں چلانا چاہیے؟",
            questionRoman = "Motorcycle kis lane me chalayein?",
            emoji = "🛣️",
            signType = SignVisualType.HELMET,
            options = listOf("بائیں جانب کی محفوظ لین میں (Left Safe Lane)", "دائیں فاسٹ لین میں (Right Fast Lane)", "سڑک کے درمیان (Center)", "زیبرا کراسنگ پر (Zebra Crossing)"),
            correctOptionIndex = 0,
            explanation = "آہستہ چلنے والی اور ہلکی گاڑیاں ہمیشہ سڑک کی بائیں لین میں چلانی چاہئیں۔"
        ),
        Question(
            id = 5,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🪞 موٹر سائیکل کے سائیڈ مررز (Side Mirrors) کیوں ضروری ہیں؟",
            questionRoman = "Side mirrors kyun zaroori hain?",
            emoji = "🪞",
            signType = SignVisualType.SIDE_MIRRORS,
            options = listOf("پیچھے سے آنے والی ٹریفک دیکھنے کے لیے (View Rear Traffic)", "خوبصورتی کے لیے (Decoration)", "تیز ہوا روکنے کے لیے (Wind Protection)", "روشنی کے لیے (Light)"),
            correctOptionIndex = 0,
            explanation = "سائیڈ شیشے پیچھے سے آنے والی گاڑیوں کی پوزیشن معلوم کرنے کے لیے نہایت ضروری ہیں۔"
        ),

        // ==========================================
        // 🚘 CAR / LTV QUESTIONS
        // ==========================================
        Question(
            id = 6,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🚘 کار چلاتے وقت سیٹ بیلٹ باندھنا کیوں ضروری ہے؟",
            questionRoman = "Car me seatbelt kyun zaroori hai?",
            emoji = "🤿",
            signType = SignVisualType.SEATBELT,
            options = listOf("حادثے کی صورت میں شدید چوٹ سے بچاؤ (Injury Protection)", "پولیس کے ڈر سے (Police Fear)", "سپیڈ بڑھانے کے لیے (Speed)", "صرف موٹر وے پر (Motorway Only)"),
            correctOptionIndex = 0,
            explanation = "سیٹ بیلٹ حادثے کی صورت میں ڈرائیور کو ونڈ سکرین سے ٹکرانے سے بچاتی ہے۔"
        ),
        Question(
            id = 7,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🚙 کار سے موڑ مڑنے سے کتنے میٹر پہلے انڈیکیٹر (Indicator) دینا چاہیے؟",
            questionRoman = "Turn lene se kitni der pehle indicator dein?",
            emoji = "💡",
            signType = SignVisualType.SIDE_MIRRORS,
            options = listOf("کم از کم 30 سے 50 میٹر پہلے (30-50 Meters Before)", "مڑتے وقت (While Turning)", "مڑنے کے بعد (After Turn)", "انڈیکیٹر کی ضرورت نہیں (No Indicator)"),
            correctOptionIndex = 0,
            explanation = "بر وقت انڈیکیٹر دینے سے پیچھے آنے والی گاڑیوں کو سنبھلنے کا موقع ملتا ہے۔"
        ),
        Question(
            id = 8,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🚗 کار پارک کرتے وقت زیبرا کراسنگ سے کتنا فاصلہ ہونا چاہیے؟",
            questionRoman = "Zebra crossing se kitna parking fasla?",
            emoji = "🅿️",
            signType = SignVisualType.PARKING_ALLOWED,
            options = listOf("کم از کم 15 میٹر یا 50 فٹ (At least 15 Meters)", "1 میٹر (1 Meter)", "زیبرا کراسنگ کے اوپر (On Zebra Crossing)", "50 میٹر (50 Meters)"),
            correctOptionIndex = 0,
            explanation = "زیبرا کراسنگ یا چوراہے کے بالکل قریب گاڑی پارک کرنا پیدل چلنے والوں کا راستہ روکتا ہے۔"
        ),
        Question(
            id = 9,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🚘 کار کا بلائنڈ سپاٹ (Blind Spot) کیا ہوتا ہے؟",
            questionRoman = "Blind spot kya hota hai?",
            emoji = "👁️",
            signType = SignVisualType.TRUCK_BLINDSPOT,
            options = listOf("شیشوں میں نظر نہ آنے والا سائیڈ کا علاقہ (Area not visible in mirrors)", "گاڑی کی چھت (Car Roof)", "کار کا بونسٹ (Car Bonnet)", "رات کا اندھیرا (Night Darkness)"),
            correctOptionIndex = 0,
            explanation = "لین بدلنے سے پہلے گردن گھما کر بلائنڈ سپاٹ چیک کرنا ضروری ہے۔"
        ),

        // ==========================================
        // 🚚 HTV / TRUCK / HEAVY VEHICLE QUESTIONS
        // ==========================================
        Question(
            id = 10,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.TRUCK_HTV,
            questionUrdu = "🚚 پاکستان موٹر وے پر ایچ ٹی وی (HTV/Truck) کی زیادہ سے زیادہ رفتار کی حد؟",
            questionRoman = "Motorway par HTV ki max speed limit?",
            emoji = "🚛",
            signType = SignVisualType.SPEEDING_FINE,
            options = listOf("110 کلومیٹر فی گھنٹہ (110 km/h)", "120 کلومیٹر فی گھنٹہ (120 km/h)", "90 کلومیٹر فی گھنٹہ (90 km/h)", "130 کلومیٹر فی گھنٹہ (130 km/h)"),
            correctOptionIndex = 0,
            explanation = "پاکستان موٹروے پر ایچ ٹی وی ٹرک اور بسوں کے لیے زیادہ سے زیادہ رفتار 110 کلومیٹر فی گھنٹہ مقرر ہے۔"
        ),
        Question(
            id = 11,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.TRUCK_HTV,
            questionUrdu = "⚖️ سرخ دائرے میں 3.5t کا نشان کس قسم کی گاڑیوں کے لیے ہے؟",
            questionRoman = "Red Circle 3.5t ka matlab?",
            emoji = "⚖️",
            signType = SignVisualType.WEIGHT_LIMIT,
            options = listOf("3.5 ٹن سے زائد وزن والی گاڑیوں کا داخلہ منع (Over 3.5t Prohibited)", "3.5 ٹن وزن لازمی (3.5t Mandatory)", "صرف کاروں کے لیے (Cars Only)", "سپیڈ لمٹ 35 (Speed 35)"),
            correctOptionIndex = 0,
            explanation = "یہ نشان بتاتا ہے کہ 3.5 ٹن سے زیادہ وزنی گاڑیاں اس سڑک پر نہیں جا سکتیں۔"
        ),
        Question(
            id = 12,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.TRUCK_HTV,
            questionUrdu = "📉 خطرناک ڈھلوان پر بھاری ٹرک / بس چلاتے وقت بریکس کی حفاظت؟",
            questionRoman = "Dhalwan par heavy vehicle kaise chalayein?",
            emoji = "⛰️",
            signType = SignVisualType.TRUCK_BRAKES,
            options = listOf("نچلا گیئر (Low Gear) استعمال کریں (Engine Braking)", "نیوٹرل کر کے بریک دبائیں (Neutral & Brake)", "کلچ دبا کر رکھیں (Hold Clutch)", "سپیڈ بڑھائیں (Speed Up)"),
            correctOptionIndex = 0,
            explanation = "ڈھلوان پر انجن بریکنگ (Low Gear) کا استعمال کریں تاکہ بریکس گرم ہو کر فیل نہ ہوں۔"
        ),
        Question(
            id = 13,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🚚 ٹرک کے پیچھے چلتے وقت کار ڈرائیور کو کتنا فاصلہ رکھنا چاہیے؟",
            questionRoman = "Truck ke peeche kitna fasla rakhein?",
            emoji = "🚛",
            signType = SignVisualType.TRUCK_BLINDSPOT,
            options = listOf("عام فاصلے سے دوگنا فاصلہ (Double Safe Distance)", "بالکل قریب (Very Close)", "1 میٹر (1 Meter)", "کوئی فاصلہ نہیں (No Gap)"),
            correctOptionIndex = 0,
            explanation = "ٹرک کے بالکل پیچھے رہنے سے آپ اس کے بلائنڈ سپاٹ میں آ جاتے ہیں اور آگے کا راستہ نظر نہیں آتا۔"
        ),

        // ==========================================
        // 🚜 TRACTOR / COMMERCIAL QUESTIONS
        // ==========================================
        Question(
            id = 14,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.COMMERCIAL_BUILDER,
            questionUrdu = "🚜 سڑک پر آہستہ چلنے والی کمرشل/ٹریکٹر گاڑیوں کے پیچھے کونسا نشان ہونا چاہیے؟",
            questionRoman = "Slow vehicle ke peeche konsa reflector?",
            emoji = "🔺",
            signType = SignVisualType.TRACTOR_LIGHTS,
            options = listOf("سرخ تکونی ریفلیکٹر (Red Triangle Reflector)", "پیلا بلب (Yellow Bulb)", "کچھ نہیں (Nothing)", "سفید کپڑا (White Cloth)"),
            correctOptionIndex = 0,
            explanation = "آہستہ رفتار گاڑیوں کے پیچھے سرخ تکونی نشان رات کے وقت حادثات سے بچاتا ہے۔"
        ),
        Question(
            id = 15,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.COMMERCIAL_BUILDER,
            questionUrdu = "👷 🚧 سڑک پر تعمیراتی کام اور بلڈر کرین کا نشان دیکھے تو؟",
            questionRoman = "Road work ka nishan dekh kar?",
            emoji = "🚧",
            signType = SignVisualType.WORK_IN_PROGRESS,
            options = listOf("رفتار کم کریں اور محتاط رہیں (Slow Down & Caution)", "سپیڈ بڑھائیں (Speed Up)", "ہارن بجاتے رہیں (Horn Continuous)", "گاڑی موڑ لیں (Turn Back)"),
            correctOptionIndex = 0,
            explanation = "تعمیراتی کام کی جگہ پر مزدور اور ہیوی مشینری موجود ہوتی ہے، رفتار کم رکھیں۔"
        ),
        Question(
            id = 16,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "📏 3m کی اونچائی کی حد کا نشان کس کے لیے اہم ہے؟",
            questionRoman = "3m height limit sign?",
            emoji = "📏",
            signType = SignVisualType.HEIGHT_LIMIT,
            options = listOf("3 میٹر سے اونچی گاڑیوں کا داخلہ منع (Height Limit 3m)", "3 میٹر لمبی گاڑی (3m Length)", "3 کلومیٹر کا راستہ (3km Distance)", "3 ٹرک (3 Trucks)"),
            correctOptionIndex = 0,
            explanation = "یہ نشان اونچے پل یا انڈر پاس سے پہلے لگایا جاتا ہے تاکہ اونچی گاڑیاں نہ ٹکرا سکیں۔"
        ),

        // ==========================================
        // 🔴 MANDATORY TRAFFIC SIGNS (لازمی نشانات)
        // ==========================================
        Question(
            id = 17,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🛑 اس سرخ ہشت پہلو نشان کا کیا مطلب ہے؟",
            questionRoman = "Red Octagon STOP sign ka matlab?",
            emoji = "🛑",
            signType = SignVisualType.STOP_OCTAGON,
            options = listOf("مکمل طور پر گاڑی روکیں (Stop Completely)", "آہستہ چلیں (Slow Down)", "مڑ جائیں (Turn)", "پارکنگ کریں (Park)"),
            correctOptionIndex = 0,
            explanation = "STOP کا نشان دیکھے تو گاڑی کو مکمل روک کر سڑک صاف ہونے پر ہی آگے بڑھیں۔"
        ),
        Question(
            id = 18,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⛔ اس نشان کا کیا مطلب ہے (سرخ دائرے میں سفید پٹی)؟",
            questionRoman = "Red circle with white bar - No Entry?",
            emoji = "⛔",
            signType = SignVisualType.NO_ENTRY,
            options = listOf("داخلہ بند ہے (No Entry)", "ون وے ختم (One Way End)", "پارکنگ کی جگہ (Parking)", "خطرناک موڑ (Danger Turn)"),
            correctOptionIndex = 0,
            explanation = "یہ نشان ون وے سڑک یا ممنوعہ علاقے میں گاڑی داخل کرنے سے روکتا ہے۔"
        ),
        Question(
            id = 19,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "➡️ نیلے دائرے میں دائیں تیر کا کیا مطلب ہے؟",
            questionRoman = "Blue Circle Right Arrow?",
            emoji = "➡️",
            signType = SignVisualType.BLUE_CIRCLE_RIGHT,
            options = listOf("دائیں مڑنا لازمی ہے (Must Turn Right)", "دائیں مڑنا منع ہے (No Right Turn)", "دائیں طرف سڑک ہے (Road on Right)", "پارکنگ دائیں ہے (Parking Right)"),
            correctOptionIndex = 0,
            explanation = "نیلا دائرہ لازمی ہدایت کا ہوتا ہے، اس لیے یہاں دائیں مڑنا قانوناً لازمی ہے۔"
        ),
        Question(
            id = 20,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⬅️ نیلے دائرے میں بائیں تیر کا کیا مطلب ہے؟",
            questionRoman = "Blue Circle Left Arrow?",
            emoji = "⬅️",
            signType = SignVisualType.BLUE_CIRCLE_LEFT,
            options = listOf("بائیں مڑنا لازمی ہے (Must Turn Left)", "بائیں مڑنا منع (No Left Turn)", "بائیں طرف دکان (Shop Left)", "آہستہ چلیں (Slow)"),
            correctOptionIndex = 0,
            explanation = "نیلے دائرے کا مطلب لازمی سمت ہے، یہاں صرف بائیں مڑ سکتے ہیں۔"
        ),
        Question(
            id = 21,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚫 اس نشان کا کیا مطلب ہے (ہارن پر سرخ لکیر)؟",
            questionRoman = "No Horn Sign?",
            emoji = "🔕",
            signType = SignVisualType.NO_HORN,
            options = listOf("ہارن بجانا منع ہے (No Horn)", "ہارن بجانا لازمی ہے (Must Horn)", "ہارن خراب ہے (Broken Horn)", "آواز تیز کریں (Volume Up)"),
            correctOptionIndex = 0,
            explanation = "ہسپتال، سکول یا خاموش علاقوں (Silent Zones) میں ہارن بجانا قانوناً ممنوع ہے۔"
        ),
        Question(
            id = 22,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⭕ سرخ دائرے میں 50 لکھا ہو تو اس کا کیا مطلب ہے؟",
            questionRoman = "Red Circle 50 Limit?",
            emoji = "5️⃣0️⃣",
            signType = SignVisualType.SPEED_LIMIT_50,
            options = listOf("زیادہ سے زیادہ رفتار 50 کلومیٹر (Max Speed 50 km/h)", "کم از کم رفتار 50 (Min Speed 50)", "50 میٹر فاصلہ (50m Gap)", "50 روپے ٹیکس (Rs 50 Tax)"),
            correctOptionIndex = 0,
            explanation = "سرخ دائرے میں نمبر سڑک کی بالائی رفتار کی حد (Maximum Speed Limit) ظاہر کرتا ہے۔"
        ),
        Question(
            id = 23,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🔵 نیلے دائرے میں سیدھا تیر کا کیا مطلب ہے؟",
            questionRoman = "Straight Only Mandatory?",
            emoji = "⬆️",
            signType = SignVisualType.GO_STRAIGHT,
            options = listOf("صرف سیدھا جانا لازمی ہے (Go Straight Only)", "مڑنا لازمی ہے (Must Turn)", "یو ٹرن لیں (Take U-Turn)", "ون وے ختم (One Way End)"),
            correctOptionIndex = 0,
            explanation = "اس نشان کی موجودگی میں دائیں یا بائیں مڑنا منع ہے، صرف سیدھا جا سکتے ہیں۔"
        ),
        Question(
            id = 24,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚷 اس نشان کا کیا مطلب ہے (چلتے ہوئے انسان پر سرخ لکیر)؟",
            questionRoman = "No Pedestrians?",
            emoji = "🚷",
            signType = SignVisualType.NO_PEDESTRIAN,
            options = listOf("پیدل چلنے والوں کا داخلہ منع ہے (No Pedestrians)", "پیدل چلنا لازمی (Must Walk)", "زیبرا کراسنگ (Zebra Crossing)", "سکول کا راستہ (School Way)"),
            correctOptionIndex = 0,
            explanation = "موٹر وے اور ایکسپریس وے پر پیدل چلنا منع ہوتا ہے۔"
        ),
        Question(
            id = 25,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚳 اس نشان کا کیا مطلب ہے (سائیکل پر سرخ لکیر)؟",
            questionRoman = "No Cyclists?",
            emoji = "🚳",
            signType = SignVisualType.NO_CYCLE,
            options = listOf("سائیکل چلانا منع ہے (No Cyclists)", "سائیکل کا ٹریک (Cycle Track)", "سائیکل اسٹینڈ (Cycle Stand)", "صرف سائیکل کے لیے (Cyclists Only)"),
            correctOptionIndex = 0,
            explanation = "یہ نشان سائیکل سواروں کو اس روٹ پر آنے سے روکتا ہے۔"
        ),
        Question(
            id = 26,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚫↩️ سرخ دائرے میں یو ٹرن پر سرخ لکیر؟",
            questionRoman = "No U-Turn?",
            emoji = "🚫↩️",
            signType = SignVisualType.NO_UTURN,
            options = listOf("یو ٹرن لینا منع ہے (No U-Turn)", "یو ٹرن لازمی ہے (Must U-Turn)", "دائیں مڑیں (Turn Right)", "بائیں مڑیں (Turn Left)"),
            correctOptionIndex = 0,
            explanation = "جس مقام پر یہ نشان ہو وہاں سے گاڑی واپس نہیں موڑ سکتے۔"
        ),
        Question(
            id = 27,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚫🚗 دو گاڑیوں پر سرخ لکیر کا کیا مطلب ہے؟",
            questionRoman = "Overtake Mana?",
            emoji = "🚫🚗",
            signType = SignVisualType.NO_OVERTAKING,
            options = listOf("اوور ٹیک کرنا منع ہے (No Overtaking)", "اوور ٹیک لازمی ہے (Must Overtake)", "دو طرفہ ٹریفک (Two Way)", "ریسنگ ایریا (Race Area)"),
            correctOptionIndex = 0,
            explanation = "خطرناک موڑ یا پل پر دوسری گاڑی سے آگے نکلنا (Overtake) منع ہوتا ہے۔"
        ),
        Question(
            id = 28,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🅿️ نیلے مربع میں حرف P کا کیا مطلب ہے؟",
            questionRoman = "Parking Allowed?",
            emoji = "🅿️",
            signType = SignVisualType.PARKING_ALLOWED,
            options = listOf("پارکنگ کی اجازت ہے (Parking Allowed)", "پارکنگ منع ہے (No Parking)", "پولیس اسٹیشن (Police Station)", "پیٹرول پمپ (Petrol Pump)"),
            correctOptionIndex = 0,
            explanation = "نیلا مربع معلوماتی نشان ہے۔ یہاں گاڑی پارک کی جا سکتی ہے۔"
        ),
        Question(
            id = 29,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚫🅿️ سرخ دائرے میں حرف P پر سرخ لکیر؟",
            questionRoman = "No Parking?",
            emoji = "🚫🅿️",
            signType = SignVisualType.NO_PARKING,
            options = listOf("گاڑی کھڑی کرنا منع ہے (No Parking)", "پارکنگ کی اجازت (Parking Allowed)", "پیٹرول دستیاب ہے (Petrol Available)", "پاسپورٹ چیک (Passport Check)"),
            correctOptionIndex = 0,
            explanation = "یہاں گاڑی پارک کرنا قانوناً ممنوع ہے اور چالان ہو سکتا ہے۔"
        ),
        Question(
            id = 30,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚫🛑 سرخ دائرے میں کراس (X) کا نشان؟",
            questionRoman = "No Stopping / Clearway?",
            emoji = "🚫🛑",
            signType = SignVisualType.NO_STOPPING,
            options = listOf("گاڑی روکنا بالکل منع ہے (No Stopping)", "گاڑی آہستہ کریں (Slow Down)", "خطرناک موڑ (Dangerous Curve)", "ریلوے پھاٹک (Railway Line)"),
            correctOptionIndex = 0,
            explanation = "اس جگہ گاڑی پارک تو دور کی بات، چند سیکنڈ کے لیے روکنا بھی منع ہے۔"
        ),
        Question(
            id = 31,
            category = QuestionCategory.MANDATORY,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🔵 نیلے دائرے میں گول تیروں کا نشان؟",
            questionRoman = "Roundabout Mandatory?",
            emoji = "🔵",
            signType = SignVisualType.ROUNDABOUT,
            options = listOf("گول چکر / چوراہا لازمی (Mandatory Roundabout)", "یو ٹرن منع ہے (No U-Turn)", "سرکلر پارکنگ (Circular Park)", "واپس مڑیں (Turn Back)"),
            correctOptionIndex = 0,
            explanation = "گول چکر میں داخل ہوتے وقت بائیں طرف سے آنے والی گاڑیوں کو راستہ دیں۔"
        ),

        // ==========================================
        // ⚠️ WARNING TRAFFIC SIGNS (انتباہی نشانات)
        // ==========================================
        Question(
            id = 32,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️🌧️ تکون میں گاڑی کے نیچے لہر دار لکیریں؟",
            questionRoman = "Phislan Wali Sarak - Slippery Road?",
            emoji = "⚠️🌧️",
            signType = SignVisualType.SLIPPERY_ROAD,
            options = listOf("پھسلن والی سڑک (Slippery Road)", "کچی سڑک (Unpaved Road)", "کار واش (Car Wash)", "سڑک پر پانی (Water on Road)"),
            correctOptionIndex = 0,
            explanation = "بارش یا تیل کی وجہ سے سڑک پر پھسلن ہو سکتی ہے، رفتار کم کریں۔"
        ),
        Question(
            id = 33,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🏫 تکون میں سکول بیگ والے بچوں کا نشان؟",
            questionRoman = "Aage School Hai - School Ahead?",
            emoji = "🏫",
            signType = SignVisualType.SCHOOL_AHEAD,
            options = listOf("آگے سکول ہے (School Ahead)", "کھیل کا میدان (Playground)", "بچوں کا پارک (Park)", "پیدل سڑک (Pedestrian)"),
            correctOptionIndex = 0,
            explanation = "سکول کے قریب گاڑی کی رفتار ہمیشہ 20-30 کلومیٹر فی گھنٹہ رکھیں کیونکہ بچے اچانک سڑک پر آ سکتے ہیں۔"
        ),
        Question(
            id = 34,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🟡 ٹریفک سگنل زرد (Yellow Light) ہو تو کیا کریں؟",
            questionRoman = "Yellow signal ka matlab?",
            emoji = "🟡",
            signType = SignVisualType.YELLOW_LIGHT,
            options = listOf("روکنے کے لیے تیار ہوں (Prepare to Stop)", "تیزی سے ریس دیں (Accelerate)", "گاڑی بند کر دیں (Engine Off)", "یو ٹرن لیں (U-Turn)"),
            correctOptionIndex = 0,
            explanation = "زرد لائٹ کا مطلب ہے کہ سرخ لائٹ ہونے والی ہے، گاڑی روکنے کی تیاری کریں۔"
        ),
        Question(
            id = 35,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️🌉 تکون میں تنگ پل (Narrow Bridge) کا نشان؟",
            questionRoman = "Narrow Bridge Ahead?",
            emoji = "🌉",
            signType = SignVisualType.NARROW_BRIDGE,
            options = listOf("آگے تنگ پل ہے (Narrow Bridge Ahead)", "پل پر سے گزرنا منع ہے (No Bridge)", "پانی کا دریا (River Ahead)", "بڑا پل (Wide Bridge)"),
            correctOptionIndex = 0,
            explanation = "تنگ پل پر ایک وقت میں دو گاڑیاں بمشکل گزر سکتی ہیں، رفتار کم کریں۔"
        ),
        Question(
            id = 36,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️🚂 تکون میں انجن کا نشان (Railway Crossing)?",
            questionRoman = "Railway crossing without barrier?",
            emoji = "🚂",
            signType = SignVisualType.RAILWAY_CROSSING,
            options = listOf("بغیر پھاٹک ریلوے کراسنگ (Un-gated Railway Line)", "ریلوے اسٹیشن (Railway Station)", "پھاٹک والا کراسنگ (Gated Railway)", "ٹرین ڈیوٹی (Train Duty)"),
            correctOptionIndex = 0,
            explanation = "بغیر پھاٹک ریلوے لائن سے پہلے رک کر دونوں طرف دیکھ کر ٹرین کی تسلی کریں۔"
        ),
        Question(
            id = 37,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️📉 تکون میں تیز ڈھلوان (Steep Descent) کا نشان؟",
            questionRoman = "Teez Dhalwan - Steep Descent?",
            emoji = "📉",
            signType = SignVisualType.STEEP_DESCENT,
            options = listOf("آگے تیز ڈھلوان ہے (Steep Descent Ahead)", "تیز چڑھائی (Steep Ascent)", "پہاڑی سڑک (Mountain Road)", "سڑک بند (Road Closed)"),
            correctOptionIndex = 0,
            explanation = "تیز ڈھلوان پر اترتے وقت گاڑی کو نچلے گیئر میں رکھیں اور بریکس پر بوجھ نہ ڈالیں۔"
        ),
        Question(
            id = 38,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️🐄 تکون میں مویشی یا جانور کا نشان؟",
            questionRoman = "Animals Crossing?",
            emoji = "🐄",
            signType = SignVisualType.ANIMALS_CROSSING,
            options = listOf("جانوروں کے گزرنے کا راستہ (Animals Crossing)", "ڈیری فارم (Dairy Farm)", "چڑیا گھر (Zoo)", "گوشت کی دکان (Meat Shop)"),
            correctOptionIndex = 0,
            explanation = "دیہاتی اور پسماندہ علاقوں میں جانور اچانک سڑک پر آ سکتے ہیں، ہارن نہ بجائیں بلکہ گاڑی آہستہ کریں۔"
        ),
        Question(
            id = 39,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️🪨 تکون میں پہاڑ سے گرتے پتھروں کا نشان؟",
            questionRoman = "Falling Rocks?",
            emoji = "🪨",
            signType = SignVisualType.FALLING_ROCKS,
            options = listOf("پتھر گرنے کا خدشہ (Falling Rocks)", "پہاڑی علاقہ (Mountain Area)", "کچی سڑک (Gravel Road)", "بارود کا دھماکہ (Explosives)"),
            correctOptionIndex = 0,
            explanation = "پہاڑی راستوں پر بارش کے بعد لینڈ سلائیڈنگ اور پتھر گرنے کا خطرہ ہوتا ہے۔"
        ),
        Question(
            id = 40,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️↪️ تکون میں دائیں طرف خطرناک موڑ؟",
            questionRoman = "Sharp Turn Right?",
            emoji = "↪️",
            signType = SignVisualType.SHARP_TURN,
            options = listOf("دائیں طرف خطرناک موڑ (Sharp Right Turn)", "دائیں جانا منع ہے (No Right)", "یو ٹرن (U-Turn)", "سیدھا راستہ (Straight)"),
            correctOptionIndex = 0,
            explanation = "خطرناک موڑ پر سائیڈ مرر دیکھیں اور رفتار مناسب حد تک کم کریں۔"
        ),
        Question(
            id = 41,
            category = QuestionCategory.WARNING,
            vehicleType = VehicleType.ALL,
            questionUrdu = "⚠️❌ تکون میں پلس (+) یا کراس کا نشان؟",
            questionRoman = "Cross Road Junction?",
            emoji = "❌",
            signType = SignVisualType.CROSS_ROAD,
            options = listOf("آگے چوراہا / کراس روڈ ہے (Cross Road Ahead)", "ہسپتال (Hospital)", "نو اینٹری (No Entry)", "ریلوے کراسنگ (Railway)"),
            correctOptionIndex = 0,
            explanation = "چوراہے پر ہر سمت سے ٹریفک آ سکتی ہے، رفتار کم کر کے دائیں بائیں دیکھیں۔"
        ),

        // ==========================================
        // 📚 GENERAL KNOWLEDGE & PAKISTAN LAWS (قوانین)
        // ==========================================
        Question(
            id = 42,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚘 اوور ٹیک (Overtake) کس طرف سے کرنا چاہیے؟",
            questionRoman = "Overtake kis taraf se karna chahiye?",
            emoji = "🚘",
            signType = SignVisualType.OVERTAKING_RULE,
            options = listOf("دائیں طرف سے (Right Side)", "بائیں طرف سے (Left Side)", "کسی بھی طرف سے (Any Side)", "صرف کچی سڑک پر (Unpaved Only)"),
            correctOptionIndex = 0,
            explanation = "پاکستان میں ڈرائیونگ دائیں جانب ہوتی ہے اس لیے ہمیشہ دائیں (Right) سے اوور ٹیک کریں۔"
        ),
        Question(
            id = 43,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚑 ایمبولینس کا سائرن سن کر کیا کرنا چاہیے؟",
            questionRoman = "Ambulance aye to kya karein?",
            emoji = "🚑",
            signType = SignVisualType.AMBULANCE,
            options = listOf("فورا بائیں ہو کر راستہ دیں (Give Way Immediately)", "آگے چلتے رہیں (Drive Ahead)", "ریس لگائیں (Race)", "ہارن دیں (Horn)"),
            correctOptionIndex = 0,
            explanation = "ایمرجنسی گاڑیوں کو فوری ترجیح پر راستہ دینا انسان دوستی اور قانونی فرض ہے۔"
        ),
        Question(
            id = 44,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚸 زیبرا کراسنگ پر پہلا حق کس کا ہے؟",
            questionRoman = "Zebra crossing pe kis ka haq?",
            emoji = "🚸",
            signType = SignVisualType.ZEBRA_CROSSING,
            options = listOf("پیدل چلنے والوں کا (Pedestrians)", "گاڑیوں کا (Cars)", "تیز گاڑی کا (Fast Vehicles)", "کسی کا نہیں (Neither)"),
            correctOptionIndex = 0,
            explanation = "زیبرا کراسنگ پر پیدل چلنے والوں کا قانوناً پہلا حق ہوتا ہے۔"
        ),
        Question(
            id = 45,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🌫️ شدید دھند (Fog) میں کونسی لائٹس استعمال کریں؟",
            questionRoman = "Fog me kaunsi light?",
            emoji = "🌫️",
            signType = SignVisualType.FOG_DRIVING,
            options = listOf("فاگ لائٹس + لو بیم (Fog Lights & Low Beam)", "ہائی بیم لائٹس (High Beam)", "صرف انڈیکیٹر (Hazard Lights)", "لائٹس بند رکھیں (No Lights)"),
            correctOptionIndex = 0,
            explanation = "دھند میں ہائی بیم چمک پیدا کرتی ہے، جبکہ فاگ لائٹ اور لو بیم سڑک واضح کرتی ہے۔"
        ),
        Question(
            id = 46,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🛞 سفر شروع کرنے سے پہلے گاڑی کی کیا چیز چیک کریں؟",
            questionRoman = "Trip se pehle kya check karein?",
            emoji = "🛞",
            signType = SignVisualType.PRE_TRIP_CHECK,
            options = listOf("ٹائر، بریک اور ایندھن (Tires, Brakes & Fuel)", "صرف میوزک پلیئر (Music Only)", "گاڑی کی چمک (Car Wash)", "کچھ نہیں (Nothing)"),
            correctOptionIndex = 0,
            explanation = "ٹائر پریشر، بریک آئل اور ایندھن کا معائنہ محفوظ سفر کی بنیاد ہے۔"
        ),
        Question(
            id = 47,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🪞 لائن بدلنے یا مڑنے سے پہلے کیا ضروری ہے؟",
            questionRoman = "Lane badalne se pehle?",
            emoji = "🪞",
            signType = SignVisualType.SIDE_MIRRORS,
            options = listOf("سائیڈ مرر دیکھیں + انڈیکیٹر دیں (Mirrors & Indicator)", "بغیر دیکھے مڑیں (Turn Blindly)", "صرف ہارن دیں (Horn Only)", "سپیڈ بڑھائیں (Speed Up)"),
            correctOptionIndex = 0,
            explanation = "انڈیکیٹر دینا اور شیشے میں دیکھنا دوسرے ڈرائیورز کو آگاہ کرتا ہے۔"
        ),
        Question(
            id = 48,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🏎️ مقررہ رفتار کی حد (Speed Limit) توڑنے پر؟",
            questionRoman = "Speed limit todne par?",
            emoji = "🏎️",
            signType = SignVisualType.SPEEDING_FINE,
            options = listOf("چالان اور قانون شکنی (Fine & Penalty)", "انعام ملتا ہے (Reward)", "کوئی فرق نہیں پڑتا (No Impact)", "صرف رات کو منع (Night Only)"),
            correctOptionIndex = 0,
            explanation = "تیز رفتاری پاکستان میں موٹر وے اور شہروں میں سب سے زیادہ حادثات کی وجہ ہے۔"
        ),
        Question(
            id = 49,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.MOTORCYCLE,
            questionUrdu = "🏍️ موٹر سائیکل چلاتے وقت ہیلمٹ کا استعمال؟",
            questionRoman = "Motorcycle helmet istemal?",
            emoji = "🏍️",
            signType = SignVisualType.HELMET,
            options = listOf("لازمی ہے (Mandatory)", "اختاری ہے (Optional)", "صرف لمبے سفر کے لیے (Long trip only)", "پولیس دیکھ کر (Only for police)"),
            correctOptionIndex = 0,
            explanation = "ہیلمٹ ڈرائیور اور پیچھے بیٹھے فرد دونوں کے لیے سر کی چوٹ سے بچاؤ کا لازمی ذریعہ ہے۔"
        ),
        Question(
            id = 50,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "📜 بغیر لائسنس گاڑی چلانے پر کیا سزا؟",
            questionRoman = "Bina license gaari chalana?",
            emoji = "📜",
            signType = SignVisualType.LICENSE_RULE,
            options = listOf("بھاری چالان اور قانونی کارروائی (Heavy Fine & Arrest)", "کوئی مسئلہ نہیں (No Problem)", "صرف شہر سے باہر منع (Out of city only)", "پہلی بار معافی (First time free)"),
            correctOptionIndex = 0,
            explanation = "ڈرائیونگ لائسنس کے بغیر گاڑی چلانا قانوناً سنگین جرم ہے اور بغیر لائسنس انشورنس بھی کلیم نہیں ہوتی۔"
        ),
        Question(
            id = 51,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "📱 گاڑی چلاتے وقت موبائل فون کا استعمال؟",
            questionRoman = "Driving me mobile phone ka istemal?",
            emoji = "📱",
            signType = SignVisualType.NO_PHONE,
            options = listOf("سخت ممنوع اور چالان (Strictly Prohibited & Fined)", "کال سننے کی اجازت (Calls Allowed)", "صرف ہینڈ فری پر (Handsfree Only)", "کوئی مسئلہ نہیں (No Issue)"),
            correctOptionIndex = 0,
            explanation = "پاکستان میں گاڑی چلاتے وقت فون استعمال کرنا توجہ ہٹاتا ہے اور اس پر چالان کاٹ دیا جاتا ہے۔"
        ),
        Question(
            id = 52,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🍺 پاکستان میں ڈرائیونگ کے دوران نشہ یا شراب نوشی؟",
            questionRoman = "Drunk driving in Pakistan?",
            emoji = "🍺",
            signType = SignVisualType.NO_DRUNK,
            options = listOf("مکمل طور پر ممنوع اور فورا قید (Zero Tolerance & Jail)", "تھوڑی اجازت (Little allowed)", "رات کو اجازت (Night allowed)", "صرف جی ٹی روڈ پر (GT Road only)"),
            correctOptionIndex = 0,
            explanation = "نشے میں گاڑی چلانا پاکستان میں انتہائی سنگین جرم ہے جس میں فورا گرفتاری اور لائسنس منسوخی ہو سکتی ہے۔"
        ),
        Question(
            id = 53,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🇵🇰 پاکستان میں موٹر کار / موٹر سائیکل لائسنس کے لیے کم از کم عمر؟",
            questionRoman = "Minimum age for car/bike license in Pakistan?",
            emoji = "🪪",
            signType = SignVisualType.LICENSE_RULE,
            options = listOf("18 سال (18 Years)", "16 سال (16 Years)", "20 سال (20 Years)", "21 سال (21 Years)"),
            correctOptionIndex = 0,
            explanation = "پاکستان میں 18 سال کی عمر میں لرنر پرمٹ اور شناختی کارڈ کی بنیاد پر لائسنس بنتا ہے۔"
        ),
        Question(
            id = 54,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🚦 سڑک پر مسلسل غیر منقطع سفید لکیر (Solid White Line) کا کیا مطلب ہے؟",
            questionRoman = "Solid white line ka matlab?",
            emoji = "🛣️",
            signType = SignVisualType.OVERTAKING_RULE,
            options = listOf("لین بدلنا یا اوورٹیک کرنا منع ہے (No Line Change / Overtake)", "اوورٹیک کی کھلی اجازت (Overtake Allowed)", "پارکنگ ایریا (Parking Area)", "سپیڈ کم کریں (Slow Down)"),
            correctOptionIndex = 0,
            explanation = "سڑک پر مسلسل سیدھی سفید لکیر اوور ٹیک کرنے اور لین بدلنے کی ممانعت کرتی ہے۔"
        ),
        Question(
            id = 55,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.ALL,
            questionUrdu = "🔄 گول چکر (Roundabout) میں داخل ہوتے وقت پہلا حق کس کا ہے؟",
            questionRoman = "Roundabout me pehla haq kis ka?",
            emoji = "🔄",
            signType = SignVisualType.ROUNDABOUT,
            options = listOf("گول چکر کے اندر (دائیں) موجود گاڑی کا (Inside / Right Traffic)", "باہر سے داخل ہونے والی گاڑی کا (Entering Vehicle)", "تیز رفتاری والی گاڑی کا (Fast Vehicle)", "بڑی گاڑی کا (Big Truck)"),
            correctOptionIndex = 0,
            explanation = "پاکستان کے ٹریفک قوانین کے تحت گول چکر میں پہلے سے موجود اور دائیں طرف کی گاڑی کا پہلا حق ہے۔"
        ),
        Question(
            id = 56,
            category = QuestionCategory.GENERAL_KNOWLEDGE,
            vehicleType = VehicleType.CAR_LTV,
            questionUrdu = "🇵🇰 پاکستان میں جی ٹی روڈ / نیشنل ہائی وے پر کار کی زیادہ سے زیادہ رفتار؟",
            questionRoman = "National Highway car max speed limit?",
            emoji = "🛣️",
            signType = SignVisualType.SPEEDING_FINE,
            options = listOf("100 کلومیٹر فی گھنٹہ (100 km/h)", "120 کلومیٹر فی گھنٹہ (120 km/h)", "80 کلومیٹر فی گھنٹہ (80 km/h)", "140 کلومیٹر فی گھنٹہ (140 km/h)"),
            correctOptionIndex = 0,
            explanation = "نیشنل ہائی وے اور جی ٹی روڈ پر کاروں کے لیے زیادہ سے زیادہ رفتار 100 کلومیٹر فی گھنٹہ مقرر ہے۔"
        )
    )
}
