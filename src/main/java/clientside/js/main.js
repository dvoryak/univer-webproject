
; (function() {
    document.addEventListener("DOMContentLoaded",function() {
        document.querySelector("#butt").addEventListener("click", send);
    });

    function send() {
        var url = "http://localhost:8080/go?data=";
        var data = document.querySelector("#data").value;
        url += data;
        var req  = new XMLHttpRequest()
        req.open('GET', url, true)
        req.onload = function () {
            if (req.readyState == 4 && req.status == "200") {
                var resp = JSON.parse(req.responseText);
                document.querySelector("#txt").value = resp.forecast[0];
            } else {
            }
        }
        req.send(null);
    }

})();