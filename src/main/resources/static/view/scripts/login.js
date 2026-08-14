async function login() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  const response = await fetch("http://localhost:8080/login", {
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
    const token = response.headers.get("Authorization");
    window.localStorage.setItem("Authorization", token)

    showToast("#okToast");

    window.setTimeout(function () {
        window.location = "/view/index.html";
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