<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Search</title>
    <#include "common-js.ftl">
</head>
<body class="mdui-theme-layout-dark ">

<div class="mdui-tab  mdui-tab-centered" mdui-tab>
    <a href="#tab1" class="mdui-ripple">Music</a>
    <a href="#tab2" class="mdui-ripple">Album</a>
    <a href="#tab3" class="mdui-ripple">Artists</a>
    <a href="#tab4" class="mdui-ripple">Seting</a>
</div>

<div id="tab1" class="mdui-container">

    <div class="box flexcolumn" style="width: 100%;">
        <div class="box flexrow" style="justify-content:center">
            <div class="mdui-textfield">
                <input  id="musicinput" class="mdui-textfield-input" style="width:400px" type="text" placeholder="Music Name"/>
            </div>
            <div class="mdui-textfield">
                <button class="mdui-btn mdui-ripple" onclick="queryMusic()">button</button>
            </div>
        </div>

        <div class="box flexrow mdui-container " style="width: 100%;justify-content:center">
            <ul class="mdui-list" id ="tab1li">


            </ul>


        </div>
    </div>


</div>

<div id="tab2" class="mdui-container">
    <div class="mdui-textfield">
        <input  id = "musicinput"class="mdui-textfield-input" type="text" placeholder="Album Name"/>
        <button class="mdui-btn mdui-ripple">OK</button>
    </div>
</div>
<div id="tab3" class="mdui-container">
    <div class="mdui-textfield">
        <input class="mdui-textfield-input" type="text" placeholder="Artists Name"/>
        <button class="mdui-btn mdui-ripple">OK</button>
    </div>
</div>
<div id="tab4" class="mdui-container">
    <div class="mdui-textfield">
        <input class="mdui-textfield-input" type="text" placeholder="Artists Name"/>
        <button class="mdui-btn mdui-ripple">OK</button>
    </div>
</div>


</body>
<script>
    var searchMusicindex=1;
    function queryMusic(){
        var  musicinputvalue = $("#musicinput").val();

        var settings = {
            "url": "127.0.0.1:8083/searchMusic/"+musicinputvalue+"/50/1",
            "method": "GET",
            "timeout": 0,
        };

        $.ajax(settings).done(function (result) {
            console.log(response);
            if(result.code==200){
                for (const lidata in result.data.abslist) {
                    songname = lidata.SONGNAME
                    artist =  lidata.ARTIST
                    var li = document.createElement("li")
                    li.className="mdui-list-item mdui-ripple liwidth"
                    $("#tab1li").append(li)
                }

            }
        });


        // $.ajax({
        //     type: "GET",
        //     url: "http://127.0.0.1:8083/searchMusic/"+musicinputvalue+"/50/"+searchMusicindex,
        //     // data: { page: $("#page").val(), limit: 5 },
        //     // dataType: "JSON",
        //     success: function(result) {
        //
        //
        //     }
        // });



        // $.ajax({url:"http://127.0.0.1:8083/searchMusic/"+musicinputvalue+"/50/"+searchMusicindex,success:function(result){
        //
        //     }});
    }



    function openbar(){
        mdui.snackbar({
            message: '3123221231231',
            position: 'top',
        });
    }
</script>
</html>