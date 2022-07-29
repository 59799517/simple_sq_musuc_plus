<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Search</title>
    <#include "common-js.ftl">
</head>
<body class="mdui-theme-layout-dark">

<div class="mdui-tab mdui-tab-centered" mdui-tab>
    <a href="#tab1" class="mdui-ripple">Music</a>
    <a href="#tab2" class="mdui-ripple">Album</a>
    <a href="#tab3" class="mdui-ripple">Artists</a>
</div>

<div id="tab1">
    <div class="mdui-textfield mdui-col-xs-6">
        <input  class="mdui-textfield-input" type="text" placeholder="Music Name"/>
    </div>
    <div>
        <input  class="mdui-btn mdui-ripple mdui-col-xs-3">OK</input>
    </div>
</div>
<div id="tab2">
    <div class="mdui-textfield">
        <input class="mdui-textfield-input" type="text" placeholder="Album Name"/>
        <button class="mdui-btn mdui-ripple">OK</button>
    </div>
</div>
<div id="tab3">
    <div class="mdui-textfield">
        <input class="mdui-textfield-input" type="text" placeholder="Artists Name"/>
        <button class="mdui-btn mdui-ripple">OK</button>
    </div>
</div>


</body>
<script>

</script>
</html>