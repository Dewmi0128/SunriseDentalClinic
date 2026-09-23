const API_BASE_URL = "http://localhost:8080/api";

let currentBill = null;


/*
 * Calculate / Generate Bill
 */
document.getElementById("billingForm").addEventListener("submit", async function (event) {

    event.preventDefault();

    const appointmentNo =
        document.getElementById("appointmentNo").value.trim();

    const message =
        document.getElementById("billingMessage");

    const billDetails =
        document.getElementById("billDetails");

    if (!appointmentNo) {

        message.style.display = "block";
        message.style.backgroundColor = "#f8d7da";
        message.style.color = "#842029";
        message.textContent = "Please enter an appointment number.";

        billDetails.style.display = "none";

        return;
    }


    try {

        message.style.display = "block";
        message.style.backgroundColor = "#fff3cd";
        message.style.color = "#664d03";
        message.textContent = "Calculating bill...";


        /*
         * Generate the bill using the Java backend.
         */
        const billResponse = await fetch(
            `${API_BASE_URL}/bills/appointment/${encodeURIComponent(appointmentNo)}`,
            {
                method: "POST"
            }
        );


        if (!billResponse.ok) {

            const errorMessage = await billResponse.text();

            throw new Error(errorMessage);
        }


        const bill = await billResponse.json();

        currentBill = bill;


        /*
         * Retrieve appointment information.
         */
        const appointmentResponse = await fetch(
            `${API_BASE_URL}/appointments/${encodeURIComponent(appointmentNo)}`
        );


        if (!appointmentResponse.ok) {

            throw new Error(
                "Unable to retrieve appointment details."
            );
        }


        const appointment =
            await appointmentResponse.json();


        /*
         * Display appointment information.
         */
        document.getElementById("displayAppointmentNo").textContent =
            bill.appointmentNo;

        document.getElementById("displayPatientName").textContent =
            appointment.patient.fullName;


        /*
         * Display treatment information.
         */
        document.getElementById("displayTreatmentName").textContent =
            appointment.treatment.treatmentName;


        document.getElementById("displayTreatmentCost").textContent =
            formatCurrency(bill.treatmentCost);


        /*
         * Display consultation fee.
         */
        document.getElementById("displayConsultationFee").textContent =
            formatCurrency(bill.consultationFee);


        /*
         * Display total amount.
         */
        document.getElementById("displayTotalAmount").textContent =
            formatCurrency(bill.totalAmount);


        /*
         * Display payment status.
         */
        document.getElementById("displayPaymentStatus").textContent =
            bill.paymentStatus;


        /*
         * Configure payment button.
         */
        updatePaymentButton(bill.paymentStatus);


        /*
         * Show bill details.
         */
        billDetails.style.display = "block";


        message.style.display = "block";
        message.style.backgroundColor = "#d1e7dd";
        message.style.color = "#0f5132";

        message.textContent =
            "Bill generated successfully. Bill ID: " +
            bill.billId;


    } catch (error) {

        console.error("Billing error:", error);

        billDetails.style.display = "none";

        message.style.display = "block";
        message.style.backgroundColor = "#f8d7da";
        message.style.color = "#842029";

        message.textContent =
            error.message ||
            "Unable to generate bill.";
    }

});



/*
 * Mark Bill as Paid
 */
document.getElementById("payButton").addEventListener("click", async function () {

    if (!currentBill) {

        showMessage(
            "Please calculate a bill first.",
            "error"
        );

        return;
    }


    if (currentBill.paymentStatus === "PAID") {

        showMessage(
            "This bill has already been paid.",
            "success"
        );

        return;
    }


    try {

        const payButton =
            document.getElementById("payButton");


        payButton.disabled = true;
        payButton.textContent = "Processing...";


        /*
         * Mark the current bill as PAID.
         */
        const paymentResponse = await fetch(
            `${API_BASE_URL}/bills/${currentBill.billId}/pay`,
            {
                method: "PUT"
            }
        );


        const result =
            await paymentResponse.text();


        if (!paymentResponse.ok) {

            throw new Error(result);
        }


        /*
         * Update local bill status.
         */
        currentBill.paymentStatus = "PAID";


        document.getElementById("displayPaymentStatus").textContent =
            "PAID";


        payButton.textContent =
            "Already Paid";


        payButton.disabled = true;


        showMessage(
            result,
            "success"
        );


    } catch (error) {

        console.error("Payment error:", error);


        const payButton =
            document.getElementById("payButton");


        payButton.disabled = false;
        payButton.textContent = "Mark as Paid";


        showMessage(
            error.message ||
            "Unable to mark bill as paid.",
            "error"
        );
    }

});



/*
 * Format currency in Sri Lankan Rupees.
 */
function formatCurrency(amount) {

    return "Rs. " +
        Number(amount).toLocaleString("en-LK", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
}



/*
 * Configure the payment button according
 * to the current payment status.
 */
function updatePaymentButton(status) {

    const payButton =
        document.getElementById("payButton");


    if (status === "PAID") {

        payButton.disabled = true;
        payButton.textContent = "Already Paid";

    } else {

        payButton.disabled = false;
        payButton.textContent = "Mark as Paid";
    }
}



/*
 * Display messages.
 */
function showMessage(messageText, type) {

    const message =
        document.getElementById("billingMessage");


    message.style.display = "block";


    if (type === "success") {

        message.style.backgroundColor = "#d1e7dd";
        message.style.color = "#0f5132";

    } else {

        message.style.backgroundColor = "#f8d7da";
        message.style.color = "#842029";
    }


    message.textContent = messageText;
}