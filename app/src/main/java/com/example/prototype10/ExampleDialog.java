package com.example.prototype10;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Data Privacy Act Of 2012")
                .setMessage("EasPay(EasyPay) puts premium value to the privacy and \n" +
                        "security of personal data entrusted by its stakeholders (e.g. students, teachers, other affiliates \n" +
                        "and non-affiliates) for legitimate purposes.\n" +
                        "We aim to comply with the Data Privacy Act of 2012 (DPA) and cooperate fully with the \n" +
                        "National Privacy Commission (NPC). We regard your privacy with utmost importance. EasPay is \n" +
                        "committed to meeting both your personal privacy, which is important to us, and ensuring that \n" +
                        "our genuine and legitimate interests as an educational institution and our ability to fully and \n" +
                        "effectively carry out our responsibilities as such are met.\n" +
                        "In this Form, the terms, “personal data” and “information” are used interchangeably. When we \n" +
                        "speak of “personal data”, the term includes the concepts of personal information, sensitive \n" +
                        "personal information, and privileged information. The first two are typically used to distinctively \n" +
                        "identify you. For their exact definitions, you may refer to the text of the Data Privacy Act.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }
}
