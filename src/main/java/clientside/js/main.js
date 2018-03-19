$(document).ready( function () {

    $("#send-btn").click(function() {
        var data = $("#data").val();

        if(checkData(data)) {
            $("#content").addClass("close");
            $("#result").removeClass("close");

            $.ajax({
                url: "http://localhost:8080/go",
                type: "get",
                data: {
                    data: "text"
                },
                success: function (resp) {
                    console.log(resp);
                },

                error : function () {
                    alert("err");
                }
            });
        } else {
            //TODO
        }

    });


    function checkData(data) {
        var pattern = /,| /;
        var arr = data.split(pattern);

        for (var i in arr) {
            var res = parseFloat(arr[i]);
            if(isNaN(res)) {
                return false;
            }
        }

        return true;
    }

    function setResult() {
        $("result ")
    }


});
