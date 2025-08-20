package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.ItemTouchHelper;   // 👈 חדש
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Recommendations.AddRecommendations;
import com.example.myapplication.Recommendations.RecommendationsAdapter;
import com.example.myapplication.standard.AddStandard;
import com.example.myapplication.standard.standardAdapter;
import com.example.myapplication.Recommendations.RecommendationsItem;
import com.example.myapplication.standard.standardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;   // 👈 חדש
import java.util.List;

public class New_problem extends AppCompatActivity {

    private AutoCompleteTextView carrierEditText, descriptionEditText;
    private EditText subTopicEditText, remarkEditText;
    private Button btnStandardItem, btnPickDate, btnPickFromGallery, btnOpenCamera, btnRecommendationsItem;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private ImagesAdapter imagesAdapter;

    private RecyclerView recyclerViewStandard, recyclerViewImages, recyclerViewRecommendations;
    private standardAdapter adapter;
    private RecommendationsAdapter adapterRecommendations;
    private ArrayList<standardItem> items;
    private ArrayList<RecommendationsItem> itemsRecommendations;

    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    // מאגר אפשרויות עבור carrierEditText
    private final List carrierOptions = Arrays.asList(
            "סלון",
            "פינת אוכל",
            "מבואה / לובי",
            "פרוזדור / מסדרון",
            "חדר אורחים",
            "חדר משפחה",
            "פינת ישיבה",
            "פטיו / מרפסת שמש",
            "גינה / חצר",
            "חדר שינה",
            "יחידת הורים",
            "חדר ילדים",
            "חדר תינוקות",
            "חדר מתבגר",
            "חדר רחצה / חדר אמבטיה",
            "שירותים / שירותי אורחים",
            "חדר ארונות",
            "מטבח",
            "מטבחון",
            "מזווה",
            "חדר כביסה",
            "חדר שירות",
            "משרד ביתי / חדר עבודה",
            "חדר משחקים",
            "חדר קולנוע ביתי",
            "ספרייה",
            "סטודיו / חדר יצירה",
            "חדר כושר",
            "מרתף",
            "עליית גג",
            "ממ\"ד (מרחב מוגן דירתי)",
            "חדר שמש (Sunroom / סולריום)",
            "חדר יינות / מרתף יינות",
            "חדר ישיבות",
            "משרד (פרטי / פתוח - Open Space)",
            "קבלה / דלפק קבלה",
            "פינת קפה / מטבחון",
            "חדר הדרכה",
            "ארכיון / גנזך",
            "חדר שרתים",
            "חדר מנוחה / חדר משחקים",
            "לובי",
            "חדר סטנדרט",
            "חדר סופריור / דלוקס",
            "סוויטה",
            "סוויטה נשיאותית",
            "חדר יחיד / זוגי / טריפל",
            "חדר אוכל",
            "אולם נשפים / אולם אירועים",
            "טרקלין עסקים (Business Lounge)",
            "ספא",
            "חדר כושר נוסף",
            "כיתת לימוד",
            "מעבדה",
            "ספרייה / חדר עיון",
            "אודיטוריום / אולם הרצאות",
            "חדר מורים",
            "חדר חזרות",
            "גלריה / אולם תצוגה",
            "חדר מחשבים",
            "חדר המתנה",
            "חדר רופא / קליניקה",
            "חדר טיפולים",
            "חדר ניתוח",
            "חדר התאוששות",
            "חדר אשפוז",
            "חדר מיון",
            // תוספות נוספות:
            "חדר מדרגות",
            "חדר שירותים ציבורי",
            "חדר תחזוקה",
            "חדר ציוד כבד",
            "חדר חימום / מזגנים",
            "חדר אחסון כללי",
            "חדר חפצי ערך",
            "חדר בטיחות / חירום",
            "חדר צפייה / גיימינג",
            "חדר אמנות / תערוכות",
            "חדר כנסים",
            "חדר מוזיקה",
            "חדר סאונה",
            "חדר דואט / חדר אירוח זוגי",
            "חדר משחקי ילדים חיצוני",
            "חדר מורים / חדר צוות",
            "חדר טיפול פיזיותרפי",
            "חדר בדיקות / מעבדה רפואית"

    );
    private final List<String> descriptionOptions = Arrays.asList(
            "סדקים נימיים בקירות",
            "סדר בקיר",
            "סדקים עמוקים בקירות",
            "סדקים אלכסוניים המעידים על שקיעת יסודות",
            "סדקים אופקיים במרכז הקיר",
            "סדקים בחיבור בין קיר לתקרה",
            "סדקים בחיבור בין קירות",
            "סדקים בתקרה",
            "קילופי צבע בקירות",
            "קילופי צבע בתקרה",
            "צבע דהוי או לא אחיד",
            "סימני מברשת או רולר בצביעה",
            "בועות או שלפוחיות בצבע",
            "נזילות צבע יבש (טפטופים)",
            "גוון צבע שונה מהמפרט",
            "טיח מתקלף",
            "טיח נפוח או חלול (נשמע חלול בהקשה)",
            "טיח גלי או לא ישר",
            "טיח מחוספס מדי או לא אחיד",
            "שפכטל לא חלק, סימני שיוף",
            "קירות עקומים",
            "פינות קיר לא ישרות (לא 90 מעלות)",
            "בליטות או שקעים בקיר",
            "חורים בקיר ממסמרים או ברגים",
            "סימני רטיבות או כתמי מים על הקירות",
            "סימני רטיבות או כתמי מים על התקרה",
            "סימני רטיבות מאחורי פאנלים",
            "עובש או טחב על הקירות",
            "עובש או טחב בפינות החדר",
            "עובש או טחב על התקרה",
            "ריח של טחב או רטיבות",
            "קורוזיה בפינות מתכת של קירות גבס",
            "חיבורים לא אחידים בין לוחות גבס",
            "ברגים בולטים בקירות גבס",
            "שקיעה של התקרה",
            "הנמכת תקרה לא ישרה או עקומה",
            "ריצוף שוקע או לא ישר",
            "אריחי ריצוף סדוקים",
            "אריחי ריצוף שבורים",
            "אריחים חסרים",
            "אריחים מנגנים (לא מקובעים היטב)",
            "אריחים עם פינות שבורות (צ'יפים)",
            "רובה מתפוררת בין האריחים",
            "רובה עם חורים או חוסרים",
            "רובה בגוון לא אחיד או שונה מהמפרט",
            "כתמים על הרובה",
            "מרווחים (פוגות) לא אחידים בין האריחים",
            "אריחים בגבהים שונים (שיניים)",
            "שאריות רובה או דבק על האריחים",
            "שיפועים לא נכונים בריצוף (במיוחד בחדרים רטובים)",
            "הצטברות מים במקומות לא רצויים",
            "רצפת פרקט שרוטה",
            "רצפת פרקט עם מרווחים גדולים מדי בין הלוחות",
            "רצפת פרקט מתנפחת ממים",
            "רצפת פרקט חורקת",
            "לוחות פרקט רופפים",
            "דהייה של צבע הפרקט",
            "פאנלים שבורים",
            "פאנלים רופפים או מנותקים מהקיר",
            "מרווח בין הפאנל לרצפה",
            "מרווח בין הפאנל לקיר",
            "חיבורי פאנלים לא אסתטיים בפינות",
            "אריחי חיפוי קיר סדוקים או שבורים",
            "אריחי חיפוי קיר רופפים",
            "אריחי חיפוי קיר עם רובה חסרה",
            "איטום לקוי סביב פתחי צנרת בקירות",
            "חדירת מים מהקירות החיצוניים (קונדנסציה)",
            "גשר קור בקירות הגורם לעיבוי",
            "בידוד תרמי לקוי",
            "בידוד אקוסטי לקוי (רעש מהשכנים או מהרחוב)",
            "אבן חיפוי חיצונית רופפת",
            "רצפת למינט עם בליטות או שקיעות",
            "כתמי חלודה על רצפת בטון מלוטש",
            "איטום לקוי בין קירות חיצוניים פינתיים",
            "קירות גבס עם בעיות יישור המתלים",
            "תקרת גבס בולטת או שוקעת בחיבורים",
            "רצפת אפוקסי עם קילופים או שרטוטים",
            "דלת לא נסגרת כראוי",
            "דלת לא ננעלת",
            "דלת נטרקת מעצמה",
            "דלת נפתחת מעצמה",
            "דלת חורקת",
            "דלת משפשפת את הרצפה",
            "דלת משפשפת את המשקוף",
            "מרווח גדול מדי בין הדלת למשקוף",
            "מרווח גדול מדי מתחת לדלת",
            "ידית דלת רופפת",
            "ידית דלת שבורה",
            "מנגנון נעילה פגום",
            "צילינדר קשה לסיבוב",
            "צבע מתקלף מהדלת",
            "שריטות או מכות בדלת",
            "משקוף דלת רופף",
            "משקוף דלת עקום או לא ישר",
            "הלבשות משקוף מנותקות או פגומות",
            "מעצור דלת חסר או שבור",
            "סף דלת שבור או רופף",
            "חלון לא נסגר כראוי",
            "חלון לא ננעל",
            "חלון קשה לפתיחה או סגירה",
            "ידית חלון רופפת או שבורה",
            "מנגנון פתיחה (קיפ) פגום",
            "זגוגית שרוטה",
            "זגוגית סדוקה",
            "זכוכית בידודית עם אדים או לכלוך בין השכבות",
            "אטמים (גומיות) יבשים, סדוקים או חסרים בחלון",
            "חדירת רוח דרך מסגרת החלון",
            "חדירת מים דרך מסגרת החלון",
            "סימני מים על אדן החלון",
            "אדן חלון פנימי סדוק או שבור",
            "אדן חלון חיצוני עם שיפוע הפוך (לכיוון החלון)",
            "מסילות חלון מלוכלכות או עקומות",
            "גלגלי הזזה של החלון שחוקים או שבורים",
            "רשת יתושים קרועה",
            "רשת יתושים חסרה",
            "מסגרת רשת עקומה",
            "תריס גלילה לא עולה או יורד",
            "תריס גלילה תקוע באמצע הדרך",
            "שלבי תריס שבורים או עקומים",
            "מנוע תריס חשמלי לא עובד",
            "מתג תריס חשמלי פגום",
            "תריס חשמלי מרעיש",
            "רצועת תריס ידני קרועה או שחוקה",
            "מנגנון גלילה ידני שבור",
            "סורגים רופפים",
            "סורגים חלודים",
            "רווח גדול מדי בין שלבי הסורגים (לא בטיחותי)",
            "דלת הזזה לא נעה על המסילות",
            "זגוגית מזוגגת עם פגיעה באיטום הפנימי",
            "תריס אלומיניום עם שלב חסר",
            "משקוף עץ נפוח ממים",
            "דלת אקורדיון תקועה או קרועה",
            "חלון עץ עם ריקבון בפרופילים",
            "ללחץ מים נמוך בברז",
            "ללחץ מים גבוה מדי (גורם להשפרצות)",
            "מים חמים לא מגיעים או מגיעים לאט",
            "מים חמים לא מספיק חמים",
            "ברז מטפטף",
            "נזילה מבסיס הברז",
            "נזילה מחיבורי הצנרת מתחת לכיור",
            "סיפון דולף",
            "ריח רע עולה מהניקוז",
            "ניקוז איטי בכיור",
            "סתימה בכיור",
            "כיור סדוק או שבור",
            "כתמים או שריטות בכיור",
            "איטום לקוי בחיבור הכיור לשיש",
            "אסלה מתנדנדת",
            "נזילה מבסיס האסלה",
            "ניאגרה דולפת (מים זורמים לאסלה כל הזמן)",
            "נזילה חיצונית מהניאגרה",
            "מנגנון הדחה לא תקין",
            "לחצן הדחה שבור או תקוע",
            "מושב אסלה שבור או רופף",
            "סדק באסלה",
            "אינטרפוץ (ברז קיר) דולף",
            "ידית אינטרפוץ קשה להזזה",
            "ראש מקלחת סתום מאבנית",
            "נזילה מצינור המקלחת",
            "ניקוז איטי במקלחון או באמבטיה",
            "שיפוע לא תקין ברצפת המקלחון (מים בורחים החוצה)",
            "דלתות מקלחון לא נסגרות הרמטית",
            "גומיות אטימה בלויות בדלתות המקלחון",
            "צירים או גלגלים של המקלחון פגומים",
            "אמבטיה שרוטה או עם פגיעות באמייל",
            "איטום לקוי בחיבור האמבטיה לקיר",
            "ארון אמבטיה נפוח מרטיבות",
            "ציפוי מתקלף מארון האמבטיה",
            "חיבור לא תקין של מכונת כביסה (נזילות)",
            "נקודת ניקוז למכונת כביסה גבוהה מדי",
            "ברז ניל דולף",
            "קולות הלם בצנרת בעת סגירת ברז",
            "צנרת רועשת",
            "חוסר בברז גז במרפסת שירות (אם נדרש)",
            "הכנה למדיח כלים חסרה או פגומה",
            "שיש מטבח סדוק",
            "שיש מוכתם",
            "חיבור לא אסתטי בין חלקי השיש",
            "איטום לקוי בין השיש לקיר",
            "פתח כיור או כיריים בשיש לא חתוך כראוי",
            "ברז תרמוסטטי לא מווסת טמפרטורה",
            "מעבר מקלחת-אמבטיה תקוע",
            "בוילר נוטף מהשסתום או הצנרת",
            "מסנן מים סתום או לא מוחלף",
            "ספיגה איטית של רצפת אמבטיה",
            "כתמי אבנית על ברזים ואביזרים",
            "ניקוז רוח מצנרת (גורמינג)",
            "שקע חשמל לא עובד",
            "שקע רופף בקיר",
            "שקע שבור או סדוק",
            "מכסה שקע חסר",
            "מיקום שקע לא נוח או לא תקני",
            "חוסר בשקעים בחדר",
            "שקע כוח חסר למכשירים יעודיים (מזגן, תנור)",
            "מפסק תאורה לא עובד",
            "מפסק רופף",
            "מתג מחליף לא מחובר נכון",
            "תאורה מהבהבת",
            "גוף תאורה לא מותקן כראוי",
            "חיווט חשוף",
            "קופסאות חשמל פתוחות ללא מכסה",
            "לוח חשמל לא מסומן כראוי (איזה מפסק שייך לאיזה אזור)",
            "מפסק פחת קופץ ללא סיבה נראית לעין",
            "ממסר פחת לא תקין (לא קופץ בלחיצת בדיקה)",
            "עומס יתר על קו חשמל מסוים",
            "הארקה לקויה",
            "שקע טלפון או רשת לא עובד",
            "נקודת טלוויזיה בכבלים לא תקינה",
            "מיקום לא נכון של נקודות תקשורת",
            "אינטרקום לא עובד",
            "פעמון דלת לא עובד",
            "גלאי עשן חסר או לא תקין",
            "שקע USB לא עובד",
            "תאורת LED מהבהבת או חלשה",
            "דימר לא מתאים לסוג הנורה",
            "חוסר נקודת חשמל למכשיר חכם",
            "כבלים חשמליים בזווית חדה",
            "ספק כוח לא מספק למערכת שמע/וידאו",
            "מזגן לא מקרר",
            "מזגן לא מחמם",
            "מזגן מטפטף מים לתוך החדר",
            "נזילה מצנרת ניקוז המזגן",
            "מזגן מרעיש באופן חריג",
            "ריח רע מהמזגן",
            "שלט מזגן לא עובד",
            "תריסי פיזור אוויר במזגן שבורים או לא זזים",
            "פילטרים מלוכלכים",
            "מיקום לא יעיל של יחידת המיזוג",
            "פתח שירות קטן מדי למזגן מיני-מרכזי",
            "תעלות מיזוג לא מבודדות כראוי",
            "רעידות של יחידת המעבה החיצונית",
            "ונטה (מפוח) בחדר רחצה לא עובדת",
            "ונטה מרעישה",
            "חוסר באוורור מספק בחדרים רטובים",
            "חימום תת-רצפתי לא עובד או לא מחמם אחיד",
            "תרמוסטט לא תקין",
            "מזגן אינברטר עם תקלות בבקר",
            "צנרת פריאון עם נזילה",
            "יחידה פנימית לא מאוזנת על הקיר",
            "מפוח תקרה עם רעידות",
            "מערכת VRV/VRF עם תקלת תקשורת",
            "דלתות ארון לא נסגרות כראוי",
            "דלתות ארון לא באותו גובה",
            "מרווחים לא אחידים בין דלתות ארון",
            "צירים חלודים או חורקים",
            "מנגנון טריקה שקטה לא עובד",
            "מגירות קשות לשליפה",
            "מגירות נופלות מהמסילה",
            "ידיות רופפות או חסרות",
            "קנטים (סיומת) של הארון מתקלפים",
            "שריטות או פגיעות בציפוי הארון",
            "מדפים עקומים או שוקעים",
            "מדפים לא מקובעים היטב לקיר",
            "ארון מטבח נפוח מרטיבות",
            "חיפוי צוקל (חלק תחתון) של ארונות מטבח פגום",
            "מבנה פנימי של הארון לא לפי התכנון",
            "חוסר במדפים",
            "מגירה עם מנגנון בלום שבור",
            "מתלה בגדים נשבר תחת עומס",
            "מדף זכוכית סדוק במתלי הקיבוע",
            "דלת ארון קלה מעץ דיקטה מתנפחת",
            "רגלי ארון מטבח לא מכווננות",
            "מעקה מרפסת נמוך מדי",
            "מעקה רופף",
            "זכוכית במעקה סדוקה",
            "מדרגות לא אחידות בגובהן",
            "קצוות חדים בפינות קיר או רהיטים",
            "סימני מכרסמים (גללים, כרסומים)",
            "סימני חרקים (תיקנים, נמלים, טרמיטים)",
            "קני ציפורים במסתור כביסה או בחלל הגג",
            "לכלוך ושאריות בנייה בתוך מסילות, ארונות או פינות",
            "אי התאמה בין התכנית האדריכלית לביצוע בפועל",
            "חומרים שונים מהמצוין במפרט הטכני",
            "גימור כללי ירוד",
            "עבודת סיליקון לא מקצועית בחדרי רחצה ובמטבח",
            "סיליקון ישן, יבש או עם עובש",
            "חלודה על אביזרי מתכת (מתלה מגבות, מוט מקלחת)",
            "מראות עם כתמים שחורים (קורוזיה)",
            "ווילונות או תריסים שלא פועלים כראוי",
            "ריצוף מרפסת ללא שיפוע ניקוז תקין",
            "פתח ניקוז במרפסת סתום",
            "גלאי פריצה לא מכוונן או חסר סוללות",
            "מערכת כיבוי אש לא מחוברת או פגומה",
            "יציאת חירום חסומה או לא מסומנת",
            "רצפה חלקה מדי (בטיחות)",
            "תאורת חירום לא עובדת",
            "מעקב על חשיפה לאסבסט במבנים ישנים",
            "כתמי שמן או דלק על רצפות מוסך",
            "ציוד כיבוי אש לא בתוקף",
            "סולם נייד לא יציב",
            "מערכת בית חכם לא מגיבה לפקודות",
            "נקודת טעינה אלחוטית לא עובדת",
            "מערכת אבטחה חכמה עם תקלות חיבור",
            "צג מגע של מערכת בקרה קפוא או לא מגיב",
            "חיישני CO2 או חומרים רעילים לא עובדים",
            "מצברים במערכות חירום פגומים",
            "מערכת מיזוג חירום לא מופעלת",
            "אינדיקטורים של מערכת חירום לא פועלים",
            "כבלי תקשורת במנהרות תשתית לא מסומנים",
            "נקודת גישה WiFi לא פועלת",
            "ריהוט שבור או לא יציב",
            "ידיות, מגירות או דלתות בריהוט נשברות או רופפות",
            "חדרי מדרגות מלוכלכים או מסוכנים",
            "מדרגות עם סימני רטיבות או חלקות",
            "חסרים פחי אשפה",
            "פחי אשפה שבורים או חלודים",
            "מרחב חנייה מסומן לא נכון",
            "מעבר חירום חסום",
            "חוסר בשלטי אזהרה",
            "שלטי דרך או שמות חדרים לא מותקנים או לא קריאים",
            "מערכת אינסטלציה ראשית רופפת",
            "צנרת ביוב ראשית לא מחוברת כראוי",
            "בלאי צנרת חיצונית",
            "נקודות חיבור למזגנים לא איטומות",
            "חורים בקיר למעבר צנרת פתוחים",
            "צנרת מים חמים קרה למגע (לא מבודדת)",
            "צנרת מים קרים חמה למגע (לא מבודדת)",
            "נזילות סמיות בצנרת קירות",
            "טפטים מתקלפים או לא ישרים",
            "קירות חיצוניים עם קילופים או פגיעות",
            "סדקים חיצוניים בקירות בטון",
            "צבע חיצוני דהוי או לא אחיד",
            "חיבורי חיפוי חיצוני רופפים",
            "אבנים חיצוניות שבורות או חסרות",
            "רצפות חוץ סדוקות או לא ישרות",
            "שיפועי חוץ לא תקינים",
            "מעקות חוץ רופפים",
            "שערים לא נפתחים או נסגרים כראוי",
            "שערים חלודים",
            "מנעולים חיצוניים לא עובדים",
            "מערכת תאורה חיצונית לא פועלת",
            "תאורת חוץ מרצפות או קירות חסרה",
            "גישה לנכים לא מסודרת",
            "רמפות לא לפי התקן",
            "תשתיות חנייה לא מסומנות נכון",
            "חוסר סימון נתיבי פינוי אש",
            "רשת ניקוז חוץ סתומה",
            "בורות ביוב חיצוניים לא מכוסים",
            "שטחי דשא או גינה לא מטופלים",
            "עצים או שיחים מסוכנים",
            "שבילים חיצוניים שבורים או לא אחידים",
            "מדרכות רופפות או שבורות",
            "סימון חניות לעיוורים או מוגבלים חסר",
            "חלקי כביש שבורים או עם בלאי",
            "תשתית תאורה ציבורית חיצונית פגומה",
            "סככות או גגות חיצוניים לא יציבים",
            "מערכת ביוב חיצונית לא מחוברת כראוי",
            "איטום חיצוני לקוי",
            "מרזבים סתומים או נשברים",
            "צנרת ניקוז חוץ עם נזילות",
            "מערכות בטיחות לא מופעלות",
            "כבלים חיצוניים חשופים או לא מסומנים",
            "מרחב פתוח עם סיכוני מעידה",
            "סימון קרקע או כביש חיצוני חסר או לא ברור",
            "חניות חשמליות לא פועלות",
            "טעינה לא מוגנת (חשמל או מים)",
            "חיבורי גז חיצוניים לא בטיחותיים",
            "צנרת גז חיצונית עם נזילה",
            "דלתות חוץ לא ננעלות",
            "דלתות חוץ נשברות או עקומות",
            "צירים חלודים או שבורים בדלתות חוץ",
            "מנעולי חוץ חלודים או לא תקינים",
            "מערכות אזעקה לא מחוברות",
            "קווי מים חיצוניים ללא בידוד",
            "נקודות חיבור חיצוניות חשופות",
            "חסרים גופי תאורה חיצוניים",
            "מפגעים סביבתיים מסוכנים",
            "חסרים מתקנים למיחזור או אשפה מסוימת",
            "גדרות חיצוניות רופפות או שבורות",
            "מערכת חניה לא מסומנת",
            "חסרים פתחי ניקוז חוץ",
            "כבלי חשמל חיצוניים חשופים",
            "תשתיות אינטרנט חיצוניות לא מחוברות",
            "ציוד חירום חיצוני חסר או לא פועל",
            "חסרים שלטי חירום חיצוניים",
            "מערכת מיזוג חיצונית לא מחוברת כראוי",
            "תשתית מים חיצונית לא תקינה",
            "ניקוז מי גשמים לקוי",
            "חסרים פתחי אוורור חיצוניים",
            "חסרים או פגומים מרזבים חיצוניים",
            "חסרים גופי מגן סביב מכשירים חיצוניים",
            "מערכות חשמל חיצוניות לא מסומנות כראוי",
            "מעליות לא תקינות",
            "מעליות עם חוסר סימון בטיחות",
            "מעליות עם רעש חריג או רטט",
            "דלתות מעלית לא נסגרות כראוי",
            "מערכת חירום במעלית לא פעילה",
            "חיישני מעלית לא עובדים",
            "כבלי מעלית חשופים",
            "מנגנון עצירת חירום במעלית פגום",
            "כבלים למעלית שחוקים",
            "צירים למעלית חלודים או שחוקים",
            "מערכת כיבוי אש במעלית לא מחוברת",
            "תאורת חירום במעלית לא פועלת",
            "מערכת תקשורת חירום במעלית לא עובדת",
            "דלתות פאניקה חסרות או לא פועלות",
            "דלתות פאניקה נתקעות",
            "מערכת פתיחת חירום ידנית פגומה",
            "חסרים מכסים או כיסויי בטיחות",
            "חסרים מחסומים זמניים בעת תחזוקה",
            "סכנות חפצים חדים או מסוכנים",
            "חסרים סימוני רצפה או מדרגות",
            "סימוני דרך לא נראים",
            "רצפה חלקה מדי מסכנת החלקה",
            "חסרים ידיות אחיזה במקומות רטובים",
            "סכנת החלקה על מדרגות רטובות",
            "חסרים סימוני חירום בנקודות קריטיות",
            "מערכת איתות חירום לא פעילה",
            "סימוני כביש פנימי פגומים או חסרים",
            "חסרים גדרות מסביב למתקנים מסוכנים",
            "סכנה ממבנה חיצוני בלתי יציב",
            "חסרים שלטי אזהרה בסביבת עבודה מסוכנת",
            "חסרים מגני בטיחות בחלונות גבוהים",
            "חסרים פעמוני חירום",
            "חסרים פחי אשפה מסומנים",
            "חסרים חיפויי רצפה מונעי החלקה",
            "חסרים סולמות חירום",
            "חסרים ציוד עזרה ראשונה",
            "חסרים גלאי עשן בחדרים מסוימים",
            "חסרים מנעולי בטיחות במקומות רגישים",
            "חסרים כבלים מגנים על מסילות חשמל",
            "חסרים או פגומים מחסומי בטיחות מסביב למכונות",
            "חסרים תמרורים פנימיים",
            "חסרים שלטים רפואיים (עזרה ראשונה)",
            "חסרים מנגנוני הגנה חיצוניים",
            "חסרים אמצעי סינון אוורור",
            "חסרים מנגנוני כיבוי אש במקומות קריטיים",
            "חסרים נקודות טעינה חירום",
            "חסרים כבלי חיבור חירום",
            "חסרים מערכות ניטור חיצוניות",
            "חסרים חיישני בטיחות",
            "חסרים מתגי חירום",
            "חסרים אמצעי איתות",
            "חסרים מגני שמש או רוחות במקומות רגישים",
            "חסרים פיתוחי גישה למתקנים חיצוניים",
            "חסרים ציוד הנדסי לבדיקת בטיחות",
            "חסרים לוחות מידע חיצוניים",
            "חסרים חיפויי קיר חיצוניים למניעת נזק",
            "חסרים מסילות גישה למתקנים חיצוניים",
            "חסרים ציוד חירום פנימי",
            "חסרים מערכות התראה פנימיות",
            "חסרים תרמומטרים ומד חום",
            "חסרים לחצני חירום",
            "חסרים כבלי איתות פנימיים",
            "חסרים מערכות תקשורת פנימיות",
            "חסרים מערכות תאורה פנימיות",
            "חסרים חיפויים פנימיים למניעת סכנה",
            "חסרים אמצעי גילוי אש פנימיים",
            "חסרים מערכות כיבוי אש פנימיות",
            "חסרים חיישני עשן פנימיים",
            "חסרים ציוד תחזוקה",
            "חסרים תרמוסטטים פנימיים",
            "חסרים מערכות בקרת לחות",
            "חסרים מערכות בקרת טמפרטורה",
            "חסרים כבלי מתח פנימיים",
            "חסרים כבלי תקשורת פנימיים",
            "חסרים מערכות אוורור פנימיות",
            "חסרים מסלולי מילוט פנימיים",
            "חסרים ציוד חירום פנימי קריטי",
            "חסרים מערכות חום מקומי",
            "חסרים מערכות מיזוג פנימיות",
            "חסרים מערכות סינון אוויר פנימיות",
            "חסרים ציוד בטיחות פנימי",
            "חסרים מערכות איתור פנימיות",
            "חסרים חיישנים פנימיים",
            "חסרים מערכות התרעה פנימיות",
            "חסרים ציוד כיבוי פנימי",
            "חסרים מערכות חירום פנימיות",
            "חסרים מערכות ניתוק פנימיות",
            "חסרים כבלי כיבוי פנימיים",
            "חסרים מערכות בידוד פנימיות",
            "חסרים מערכות ניטור פנימיות",
            "חסרים מערכות אבטחה פנימיות",
            "חסרים כבלי אבטחה פנימיים",
            "חסרים מערכות חיסום פנימיות",
            "חסרים מערכות פינוי פנימיות",
            "חסרים מערכות גילוי פנימיות",
            "חסרים כבלי גילוי פנימיים",
            "חסרים מערכות זיהוי פנימיות",
            "חסרים מערכות פיקוח פנימיות",
            "חסרים מערכות הגנה פנימיות",
            "חסרים מערכות ניטור חיצוניות"

    );

    private ArrayAdapter<String> carrierAdapter;

    public static final int ADD_STANDARD_REQUEST = 1;
    public static final int EDIT_ITEM_REQUEST = 2;
    public static final int ADD_RECOMMENDATION_REQUEST = 3;
    public static final int EDIT_ITEM_RECOMMENDATION_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

        carrierEditText = findViewById(R.id.carrierEditText);
        subTopicEditText = findViewById(R.id.SubTopicEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        remarkEditText = findViewById(R.id.remarkEditText);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickFromGallery = findViewById(R.id.btnSelectImage);
        btnOpenCamera = findViewById(R.id.btnCaptureImage);
        btnStandardItem = findViewById(R.id.btnStandardItem);
        recyclerViewStandard = findViewById(R.id.recyclerViewStandard);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewRecommendations = findViewById(R.id.recyclerViewRecommendations);
        btnRecommendationsItem = findViewById(R.id.btnRecommendationsItem);

        // ------------------------
        // AutoCompleteTextView setup
        // ------------------------
        carrierAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, carrierOptions);
        carrierEditText.setAdapter(carrierAdapter);
        carrierEditText.setOnClickListener(v -> carrierEditText.showDropDown());

        ArrayAdapter<String> descriptionAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, descriptionOptions);
        descriptionEditText.setAdapter(descriptionAdapter);
        descriptionEditText.setOnClickListener(v -> descriptionEditText.showDropDown());

        // ------------------------
        // RecyclerView סטנדרטים
        // ------------------------
        items = new ArrayList<>();
        adapter = new standardAdapter(this, items);
        recyclerViewStandard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStandard.setAdapter(adapter);
        updateRecyclerViewVisibility();

        // ✅ הוספת ItemTouchHelper לגרירת standard items
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();

                // החלפת מקומות במערך
                Collections.swap(items, fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // לא רוצים סווייפ למחיקה
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewStandard);

        // ------------------------
        // RecyclerView המלצות
        // ------------------------
        itemsRecommendations = new ArrayList<>();
        adapterRecommendations = new RecommendationsAdapter(this, itemsRecommendations);
        recyclerViewRecommendations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommendations.setAdapter(adapterRecommendations);
        updateRecyclerViewRecommendationsVisibility();

        btnStandardItem.setOnClickListener(v -> {
            Intent intent = new Intent(New_problem.this, AddStandard.class);
            startActivityForResult(intent, ADD_STANDARD_REQUEST);
        });

        btnRecommendationsItem.setOnClickListener(v -> {
            Intent intent1 = new Intent(New_problem.this, AddRecommendations.class);
            startActivityForResult(intent1, ADD_RECOMMENDATION_REQUEST);
        });

        // ------------------------
        // RecyclerView תמונות
        // ------------------------
        imagesAdapter = new ImagesAdapter(this, selectedImages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setAdapter(imagesAdapter);
        updateRecyclerViewImagesVisibility();

        btnPickFromGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryLauncher.launch(intent);
        });

        btnOpenCamera.setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraActivity.class);
            cameraLauncher.launch(intent);
        });

        // ------------------------
        // Date picker
        // ------------------------
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new android.app.DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format("%02d/%02d/%04d",
                                dayOfMonth, month1 + 1, year1);
                        btnPickDate.setText(selectedDate);
                    }, year, month, day).show();
        });

        // ------------------------
        // ActivityResultLaunchers
        // ------------------------
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        if (result.getData().getClipData() != null) {
                            int count = result.getData().getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                selectedImages.add(imageUri);
                                imagesAdapter.notifyItemInserted(selectedImages.size() - 1);
                            }
                        } else if (result.getData().getData() != null) {
                            selectedImages.add(result.getData().getData());
                            imagesAdapter.notifyItemInserted(selectedImages.size() - 1);
                        }
                        updateRecyclerViewImagesVisibility();
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<Uri> cameraImages = result.getData().getParcelableArrayListExtra("capturedImages");
                        if (cameraImages != null) {
                            int startIndex = selectedImages.size();
                            selectedImages.addAll(cameraImages);
                            imagesAdapter.notifyItemRangeInserted(startIndex, cameraImages.size());
                            updateRecyclerViewImagesVisibility();
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == ADD_STANDARD_REQUEST) {
            String standard = data.getStringExtra("standard");
            ArrayList<Uri> newImages = data.getParcelableArrayListExtra("selectedImages");
            standardItem newItem = new standardItem(standard);
            if (newImages != null) newItem.setImages(newImages);
            items.add(newItem);
            adapter.notifyItemInserted(items.size() - 1);
            updateRecyclerViewVisibility();
        }

        if (requestCode == ADD_RECOMMENDATION_REQUEST) {
            String description = data.getStringExtra("description");
            String amount = data.getStringExtra("amount");
            String unitPrice = data.getStringExtra("unitPrice");
            String unit = data.getStringExtra("unit");
            String totalPrice = data.getStringExtra("totalPrice");

            RecommendationsItem newItem = new RecommendationsItem(description, amount, unitPrice, unit, totalPrice);
            itemsRecommendations.add(newItem);
            adapterRecommendations.notifyItemInserted(itemsRecommendations.size() - 1);
            updateRecyclerViewRecommendationsVisibility();
        }

        if (requestCode == EDIT_ITEM_REQUEST) {
            int position = data.getIntExtra("position", -1);
            String standard = data.getStringExtra("standard");
            ArrayList<Uri> updatedImages = data.getParcelableArrayListExtra("selectedImages");

            if (position != -1) {
                standardItem updatedItem = items.get(position);
                updatedItem.setStandard(standard);
                if (updatedImages != null) {
                    updatedItem.setImages(updatedImages);
                }
                adapter.notifyItemChanged(position);
                updateRecyclerViewVisibility();
            }
        }

        if (requestCode == EDIT_ITEM_RECOMMENDATION_REQUEST) {
            int position1 = data.getIntExtra("position1", -1);
            String description = data.getStringExtra("description");
            String amount = data.getStringExtra("amount");
            String unitPrice = data.getStringExtra("unitPrice");
            String unit = data.getStringExtra("unit");
            String totalPrice = data.getStringExtra("totalPrice");

            if (position1 != -1) {
                RecommendationsItem updatedItem = itemsRecommendations.get(position1);
                updatedItem.setDescription(description);
                updatedItem.setAmount(amount);
                updatedItem.setUnitPrice(unitPrice);
                updatedItem.setUnit(unit);
                updatedItem.setTotalPrice(totalPrice);
                adapterRecommendations.notifyItemChanged(position1);
                updateRecyclerViewRecommendationsVisibility();
            }
        }
    }

    public void updateRecyclerViewVisibility() {
        recyclerViewStandard.setVisibility(adapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    public void updateRecyclerViewRecommendationsVisibility() {
        recyclerViewRecommendations.setVisibility(adapterRecommendations.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }

    public void updateRecyclerViewImagesVisibility() {
        recyclerViewImages.setVisibility(imagesAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
    }
}
