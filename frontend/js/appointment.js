const API_BASE_URL = "http://localhost:8080/api";

const appointmentForm = document.getElementById("appointmentForm");

const newPatientRadio = document.getElementById("newPatientRadio");
const existingPatientRadio = document.getElementById("existingPatientRadio");

const newPatientSection = document.getElementById("newPatientSection");
const existingPatientSection = document.getElementById("existingPatientSection");

const patientSelect = document.getElementById("patientSelect");
const dentistSelect = document.getElementById("dentistSelect");
const treatmentSelect = document.getElementById("treatmentSelect");

const appointmentMessage =
    document.getElementById("appointmentMessage");


// ---------------------------------------------------------
// PAGE LOAD
// ---------------------------------------------------------

document.addEventListener("DOMContentLoaded", function () {

    loadPatients();
    loadDentists();
    loadTreatments();

    newPatientRadio.addEventListener("change", updatePatientMode);
    existingPatientRadio.addEventListener("change", updatePatientMode);

    updatePatientMode();
});


// ---------------------------------------------------------
// PATIENT MODE
// ---------------------------------------------------------

function updatePatientMode() {

    if (newPatientRadio.checked) {

        newPatientSection.classList.remove("hidden");
        existingPatientSection.classList.add("hidden");

        document.getElementById("patientName").required = true;
        document.getElementById("address").required = true;
        document.getElementById("contactNumber").required = true;

        patientSelect.required = false;

    } else {

        newPatientSection.classList.add("hidden");
        existingPatientSection.classList.remove("hidden");

        document.getElementById("patientName").required = false;
        document.getElementById("address").required = false;
        document.getElementById("contactNumber").required = false;

        patientSelect.required = true;
    }
}


// ---------------------------------------------------------
// LOAD EXISTING PATIENTS
// ---------------------------------------------------------

async function loadPatients() {

    try {

        const response = await fetch(`${API_BASE_URL}/patients`);

        if (!response.ok) {
            throw new Error("Unable to load patients");
        }

        const patients = await response.json();

        patientSelect.innerHTML =
            '<option value="">Select Patient</option>';

        patients.forEach(patient => {

            const option = document.createElement("option");

            option.value = patient.patientId;

            option.textContent =
                `${patient.fullName} - ${patient.contactNumber}`;

            patientSelect.appendChild(option);
        });

    } catch (error) {

        console.error("Patient loading error:", error);

        patientSelect.innerHTML =
            '<option value="">Unable to load patients</option>';
    }
}


// ---------------------------------------------------------
// LOAD DENTISTS
// ---------------------------------------------------------

async function loadDentists() {

    try {

        const response =
            await fetch(`${API_BASE_URL}/dentists`);

        if (!response.ok) {
            throw new Error("Unable to load dentists");
        }

        const dentists = await response.json();

        dentistSelect.innerHTML =
            '<option value="">Select Dentist</option>';

        dentists.forEach(dentist => {

            const option = document.createElement("option");

            option.value = dentist.dentistId;

            option.textContent =
                `${dentist.fullName} - ${dentist.specialization}`;

            dentistSelect.appendChild(option);
        });

    } catch (error) {

        console.error("Dentist loading error:", error);

        dentistSelect.innerHTML =
            '<option value="">Unable to load dentists</option>';
    }
}


// ---------------------------------------------------------
// LOAD TREATMENTS
// ---------------------------------------------------------

async function loadTreatments() {

    try {

        const response =
            await fetch(`${API_BASE_URL}/treatments`);

        if (!response.ok) {
            throw new Error("Unable to load treatments");
        }

        const treatments = await response.json();

        treatmentSelect.innerHTML =
            '<option value="">Select Treatment</option>';

        treatments.forEach(treatment => {

            const option = document.createElement("option");

            option.value = treatment.treatmentId;

            option.textContent =
                `${treatment.treatmentName} - Rs. ${treatment.cost}`;

            treatmentSelect.appendChild(option);
        });

    } catch (error) {

        console.error("Treatment loading error:", error);

        treatmentSelect.innerHTML =
            '<option value="">Unable to load treatments</option>';
    }
}


// ---------------------------------------------------------
// FORM SUBMISSION
// ---------------------------------------------------------

appointmentForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    showMessage("", "");

    try {

        const appointmentNo =
            document.getElementById("appointmentNo").value.trim();

        const appointmentDate =
            document.getElementById("appointmentDate").value;

        const appointmentTime =
            document.getElementById("appointmentTime").value;

        const dentistId =
            parseInt(dentistSelect.value);

        const treatmentId =
            parseInt(treatmentSelect.value);

        const notes =
            document.getElementById("notes").value.trim();


        // Basic validation

        if (!appointmentNo) {
            showMessage(
                "Please enter an appointment number.",
                "error"
            );
            return;
        }

        if (!appointmentDate) {
            showMessage(
                "Please select an appointment date.",
                "error"
            );
            return;
        }

        if (!appointmentTime) {
            showMessage(
                "Please select an appointment time.",
                "error"
            );
            return;
        }

        if (!dentistId || !treatmentId) {
            showMessage(
                "Please select a dentist and treatment.",
                "error"
            );
            return;
        }


        // -------------------------------------------------
        // NEW PATIENT
        // -------------------------------------------------

        let patientId;

        if (newPatientRadio.checked) {

            const patientName =
                document.getElementById("patientName").value.trim();

            const address =
                document.getElementById("address").value.trim();

            const contactNumber =
                document.getElementById("contactNumber").value.trim();

            const dateOfBirth =
                document.getElementById("dateOfBirth").value;

            const gender =
                document.getElementById("gender").value;


            if (!patientName || !address || !contactNumber) {

                showMessage(
                    "Please enter the patient's name, address and contact number.",
                    "error"
                );

                return;
            }


            // Validate Sri Lankan-style mobile number

            if (!/^0\d{9}$/.test(contactNumber)) {

                showMessage(
                    "Please enter a valid 10-digit contact number.",
                    "error"
                );

                return;
            }


            const patientData = {

                fullName: patientName,

                address: address,

                contactNumber: contactNumber,

                dateOfBirth:
                    dateOfBirth || null,

                gender:
                    gender || null
            };


            showMessage(
                "Saving new patient...",
                "info"
            );


            // Save patient

            const patientResponse = await fetch(
                `${API_BASE_URL}/patients`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(patientData)
                }
            );


            if (!patientResponse.ok) {

                throw new Error(
                    "Patient registration failed."
                );
            }


            /*
             * The current PatientController returns a success
             * message rather than the generated patient ID.
             *
             * Therefore, reload the patient list and identify
             * the newly registered patient using its details.
             */

            const patientsResponse =
                await fetch(`${API_BASE_URL}/patients`);

            if (!patientsResponse.ok) {
                throw new Error(
                    "Unable to retrieve the newly registered patient."
                );
            }

            const patients =
                await patientsResponse.json();


            const matchingPatient =
                patients.find(patient =>
                    patient.fullName === patientName &&
                    patient.contactNumber === contactNumber &&
                    patient.address === address
                );


            if (!matchingPatient) {

                throw new Error(
                    "Patient was saved, but the patient ID could not be identified."
                );
            }


            patientId =
                matchingPatient.patientId;

        }


            // -------------------------------------------------
            // EXISTING PATIENT
        // -------------------------------------------------

        else {

            patientId =
                parseInt(patientSelect.value);

            if (!patientId) {

                showMessage(
                    "Please select an existing patient.",
                    "error"
                );

                return;
            }
        }


        // -------------------------------------------------
        // REGISTER APPOINTMENT
        // -------------------------------------------------

        const appointmentData = {

            appointmentNo: appointmentNo,

            patientId: patientId,

            dentistId: dentistId,

            treatmentId: treatmentId,

            appointmentDate: appointmentDate,

            appointmentTime: appointmentTime,

            status: "SCHEDULED",

            notes: notes
        };


        showMessage(
            "Registering appointment...",
            "info"
        );


        const appointmentResponse =
            await fetch(
                `${API_BASE_URL}/appointments`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(appointmentData)
                }
            );


        const result =
            await appointmentResponse.text();


        if (!appointmentResponse.ok) {

            throw new Error(
                result || "Appointment registration failed."
            );
        }


        if (
            result.toLowerCase().includes("success")
        ) {

            showMessage(
                `Appointment ${appointmentNo} registered successfully!`,
                "success"
            );

            appointmentForm.reset();

            newPatientRadio.checked = true;

            updatePatientMode();

            await loadPatients();

        } else {

            showMessage(
                result || "Unable to register appointment.",
                "error"
            );
        }


    } catch (error) {

        console.error(
            "Appointment registration error:",
            error
        );

        showMessage(
            error.message ||
            "An unexpected error occurred.",
            "error"
        );
    }
});


// ---------------------------------------------------------
// MESSAGE DISPLAY
// ---------------------------------------------------------

function showMessage(message, type) {

    appointmentMessage.textContent = message;

    appointmentMessage.className = "";

    if (type) {
        appointmentMessage.classList.add(
            `message-${type}`
        );
    }
}