package com.example.visahackathon;

//import android.os.Environment;
import com.visa.checkout.Environment;


import com.visa.checkout.Profile;
import com.visa.checkout.PurchaseInfo;
import com.visa.checkout.VisaConfigRequest;
import com.visa.checkout.VisaConfigResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
//import com.braintreepayments.api.VisaCheckout.*;

public class ConfigurePayment {
    public static Profile getProfile(String place) {
        return new Profile.ProfileBuilder("PRK3683JPAH1PZ4OVBG621jVMPOhWmW1Xi1nI5THB9zpnJk7U",
                Environment.SANDBOX).setProfileName(place)
                .build();
    }

    public static PurchaseInfo getPurchaseInfo(String amount, String comment) {
        HashMap<String, String> data = new HashMap<>();
        data.put("key", "value");
        data.put("key1", "value1");
        Double transactionAmount = Double.parseDouble(amount);
        return new PurchaseInfo.PurchaseInfoBuilder(new BigDecimal(transactionAmount),
                PurchaseInfo.Currency.USD).setShippingHandling(new BigDecimal("0"))
                .setTax(new BigDecimal("0"))
                .setDiscount(new BigDecimal("0"))
                .setMisc(new BigDecimal("0"))
                .setGiftWrap(new BigDecimal("0"))
                .setDescription(comment)
                .setOrderId("234-SD355-343432")
                .setReviewMessage(comment)
                .setMerchantRequestId("345345345dsfs434343423234234")
                .setSourceId("test-source-id")
                .setPromoCode("test-promo-code")
                .setShippingAddressRequired(true)
                .setUserReviewAction(PurchaseInfo.UserReviewAction.PAY)
                .setThreeDSSetup(true, false)
                .setCustomData(data)
                .setPrefillRequest(new VisaConfigRequest() {
                    @Override
                    public void handleConfigRequest(Object o, VisaConfigResponse visaConfigResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("userFirstName", "First name");
                            jsonObject.put("userLastName", "Last name");
                            jsonObject.put("userEmail", "email");
                            jsonObject.put("userPhone", "phone");
                            visaConfigResponse.sendResponse(jsonObject);
                        } catch (JSONException e) {
                            visaConfigResponse.sendResponse(null);
                        }
                    }
                })
                .put("any key", "any data")
                .build();
    }


}
