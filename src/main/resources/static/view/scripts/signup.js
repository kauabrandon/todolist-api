async function signup() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  const response = await fetch("http://localhost:8080/user", {
    method: "POST",
    headers: new Headers({
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
    }),
    body: JSON.stringify({
      username: username,
      password: password,
    }),
  });

  if (response.ok) {
    showToast("#okToast");
    window.setTimeout(function () {
        window.location = "/view/login.html";
        }, 1500);
  } else {
    showToast("#errorToast");
  }
}

function showToast(id) {
  const toastElList = [].slice.call(document.querySelectorAll(id));
  const toastList = toastElList.map(function (toastEl) {
    return new bootstrap.Toast(toastEl);
  });
  toastList.forEach((toast) => toast.show());
}