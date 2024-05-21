// 1. Regular expression to check if value is empty or a white space: https://www.freecodecamp.org/news/how-to-include-empty-string-in-regex/
// The test() method of RegExp instances executes a search with this regular expression for a match between a regular expression and a specified string.

function isEmpty(value){
    var regex = new RegExp(/^(?=\s*$)/g);
    return (!value || regex.test(value));
};

// 2. Example regular expression: isEmail
// Reference: https://emaillistvalidation.com/blog/mastering-email-validation-in-bootstrap-5-a-comprehensive-guide/

function isEmail(value){
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(value);
}

// 3. Regular expression to check if value meets the password criteria
// Reference: https://dev.to/rasaf_ibrahim/write-regex-password-validation-like-a-pro-5175

function isPassword(value){
    var regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regex.test(value);
}

// 4. Show the toast according to the result, used across all pages (DRY)
function showToast({toastElement, toastBodyElement, bgColor, msg}){
    // Run BootStrap5's toast to show the activity is complete.
    const toastEl = toastElement;
    const toastBody = toastBodyElement;
    toastEl.classList.remove("bg-success"); //remove all known and used colors here first
    toastEl.classList.remove("bg-danger");  //remove all known and used colors here first
    toastEl.classList.add(`bg-${bgColor}`);
    toastEl.classList.add("text-white");
    toastBody.textContent = msg;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}

// 5. Log in to server
async function logIn({email, password, api}){

    const request = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    };

    // send email and password to server
    const response = await fetch(api, request);
    const status = response.status;
    const data = await response.json();

    // Return the result only if the status is 200 (OK), else return false
    return status === 200 ? data : false;

}

// For unit testing
// module.exports = {isEmpty, isEmail};

// Further reading: 
// 1. Puppeteer library: https://jestjs.io/docs/puppeteer
// 2. Regular expression generator: https://saasbase.dev/tools/regex-generator
// 3. NTU examples: https://www3.ntu.edu.sg/home/ehchua/programming/howto/Regexe.html
// 4. https://jestjs.io/docs/asynchronous
// 4.1. Asynchronous functions: https://jestjs.io/docs/tutorial-async