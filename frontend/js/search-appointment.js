const API_BASE_URL = "http://localhost:8080/api";

let currentAppointmentNo = "";

// =========================================================
// SEARCH APPOINTMENT
// =========================================================

document.getElementById("searchForm").addEventListener("submit", async function (event) {

    event.preventDefault();

    const appointmentNo =
        document.getElementById("appointmentNo").value.trim();

    const message =
        document.getElementById("searchMessage");

    const details =
        document.getElementById("appointmentDetails");

    const cancelSection =
        document.getElementById("cancelSection");

    if (!appointmentNo) {

        message.style.display = "block";
        message.style.backgroundColor = "#f8d7da";
        message.style.color = "#842029";
        message.textContent =
            "Please enter an appointment number.";

        return;
    }

    // Hide old details
    details.style.display = "none";
    cancelSection.classList.add("hidden");

    currentAppointmentNo = "";


    try {

        message.style.display = "block";
        message.style.backgroundColor = "#fff3cd";
        message.style.color = "#664d03";
        message.textContent =
            "Searching appointment...";


        const response = await fetch(
            `${API_BASE_URL}/appointments/${encodeURIComponent(appointmentNo)}`
        );


        if (!response.ok) {

            message.style.backgroundColor = "#f8d7da";
            message.style.color = "#842029";
            message.textContent =
                "Appointment not found. Please check the appointment number.";

            return;
        }


        const appointment = await response.json();


        if (!appointment || !appointment.appointmentNo) {

            message.style.backgroundColor = "#f8d7da";
            message.style.color = "#842029";
            message.textContent =
                "Appointment not found. Please check the appointment number.";

            return;
        }


        // Store appointment number for cancellation
        currentAppointmentNo =
            appointment.appointmentNo;


        // =================================================
        // APPOINTMENT INFORMATION
        // =================================================

        document.getElementById("displayAppointmentNo").textContent =
            appointment.appointmentNo;

        document.getElementById("displayStatus").textContent =
            appointment.status;


        // =================================================
        // PATIENT INFORMATION
        // =================================================

        document.getElementById("displayPatientName").textContent =
            appointment.patient.fullName;

        document.getElementById("displayPatientContact").textContent =
            appointment.patient.contactNumber;

        document.getElementById("displayPatientAddress").textContent =
            appointment.patient.address;

        document.getElementById("displayPatientDob").textContent =
            appointment.patient.dateOfBirth || "Not available";

        document.getElementById("displayPatientGender").textContent =
            appointment.patient.gender || "Not available";


        // =================================================
        // DENTIST INFORMATION
        // =================================================

        document.getElementById("displayDentistName").textContent =
            appointment.dentist.fullName;

        document.getElementById("displayDentistSpecialization").textContent =
            appointment.dentist.specialization;

        document.getElementById("displayDentistContact").textContent =
            appointment.dentist.contactNumber;

        document.getElementById("displayDentistAvailability").textContent =
            appointment.dentist.availability;


        // =================================================
        // TREATMENT INFORMATION
        // =================================================

        document.getElementById("displayTreatmentName").textContent =
            appointment.treatment.treatmentName;

        document.getElementById("displayTreatmentCost").textContent =
            "Rs. " +
            Number(appointment.treatment.cost).toLocaleString();

        document.getElementById("displayTreatmentDescription").textContent =
            appointment.treatment.description;


        // =================================================
        // APPOINTMENT SCHEDULE
        // =================================================

        document.getElementById("displayAppointmentDate").textContent =
            appointment.appointmentDate;

        document.getElementById("displayAppointmentTime").textContent =
            appointment.appointmentTime;

        document.getElementById("displayNotes").textContent =
            appointment.notes || "No notes";


        // =================================================
        // SHOW DETAILS
        // =================================================

        details.style.display = "block";


        // =================================================
        // SHOW CANCEL BUTTON ONLY IF SCHEDULED
        // =================================================

        if (
            appointment.status &&
            appointment.status.toUpperCase() === "SCHEDULED"
        ) {

            cancelSection.classList.remove("hidden");
        }


        message.style.backgroundColor = "#d1e7dd";
        message.style.color = "#0f5132";
        message.textContent =
            "Appointment found successfully.";


    } catch (error) {

        console.error(
            "Appointment search error:",
            error
        );

        message.style.display = "block";
        message.style.backgroundColor = "#f8d7da";
        message.style.color = "#842029";

        message.textContent =
            "Unable to connect to the server.";
    }

});


// =========================================================
// CANCEL APPOINTMENT
// =========================================================

document.getElementById("cancelAppointmentButton")
    .addEventListener("click", async function () {

        const cancelButton = this;

        const message =
            document.getElementById("searchMessage");

        const cancelSection =
            document.getElementById("cancelSection");


        if (!currentAppointmentNo) {

            alert(
                "Please search for an appointment first."
            );

            return;
        }


        const confirmed = confirm(
            "Are you sure you want to cancel appointment " +
            currentAppointmentNo +
            "?"
        );


        if (!confirmed) {
            return;
        }


        cancelButton.disabled = true;

        cancelButton.textContent =
            "Cancelling...";


        try {

            const response = await fetch(
                `${API_BASE_URL}/appointments/${encodeURIComponent(currentAppointmentNo)}/cancel`,
                {
                    method: "PUT"
                }
            );


            const result =
                await response.text();


            if (!response.ok) {

                message.style.display = "block";
                message.style.backgroundColor = "#f8d7da";
                message.style.color = "#842029";

                message.textContent =
                    result ||
                    "Unable to cancel appointment.";

                cancelButton.disabled = false;

                cancelButton.textContent =
                    "Cancel Appointment";

                return;
            }


            // Update appointment status on screen
            document.getElementById("displayStatus").textContent =
                "CANCELLED";


            // Hide button after successful cancellation
            cancelSection.classList.add("hidden");


            message.style.display = "block";
            message.style.backgroundColor = "#d1e7dd";
            message.style.color = "#0f5132";

            message.textContent =
                "Appointment " +
                currentAppointmentNo +
                " cancelled successfully.";


        } catch (error) {

            console.error(
                "Cancellation error:",
                error
            );

            message.style.display = "block";
            message.style.backgroundColor = "#f8d7da";
            message.style.color = "#842029";

            message.textContent =
                "Unable to connect to the server.";

            cancelButton.disabled = false;

            cancelButton.textContent =
                "Cancel Appointment";
        }

    });