<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="global_assets/dashboard.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">


</head>
<body>

        <div class="header_div navbar-spacers">
            <?php include "header.php"; ?>
            <!-- Use jquery to insert header content here if PHP fails    -->
        </div>

        <!-- Body of ticker -->
        <div class="">
                        <div class="row">
                                <div class="offset-lg-2 col-lg-5 bar-right">
                                        <h3 id="company_name">Stock Name Here</h3>
                                                <div>
                                                        <canvas id="myChart" height="400"></canvas>
                                                        <script>
                                                        var ctx = document.getElementById('myChart').getContext('2d');

                                                        // need to change this to be dynamic
                                                        function generateTable() {
                                                                return [
                                                                        {"date":"2019-04-24 09:30:00.0","marketAverage":"207.344","marketVolume":"499499","marketNumberOfTrades":"946"},
                                                                        {"date":"2019-04-24 09:31:00.0","marketAverage":"208.039","marketVolume":"311879","marketNumberOfTrades":"1308"},
                                                                        {"date":"2019-04-24 09:32:00.0","marketAverage":"208.142","marketVolume":"262091","marketNumberOfTrades":"991"},
                                                                        {"date":"2019-04-24 09:33:00.0","marketAverage":"207.997","marketVolume":"121823","marketNumberOfTrades":"667"},
                                                                        {"date":"2019-04-24 09:34:00.0","marketAverage":"208.111","marketVolume":"145996","marketNumberOfTrades":"666"},
                                                                        {"date":"2019-04-24 09:35:00.0","marketAverage":"207.88","marketVolume":"180003","marketNumberOfTrades":"799"},
                                                                        {"date":"2019-04-24 09:36:00.0","marketAverage":"208.009","marketVolume":"123262","marketNumberOfTrades":"750"},
                                                                        {"date":"2019-04-24 09:37:00.0","marketAverage":"207.832","marketVolume":"96744","marketNumberOfTrades":"486"},
                                                                        {"date":"2019-04-24 09:38:00.0","marketAverage":"208.067","marketVolume":"164621","marketNumberOfTrades":"890"},
                                                                        {"date":"2019-04-24 09:39:00.0","marketAverage":"208.161","marketVolume":"62061","marketNumberOfTrades":"366"},
                                                                        {"date":"2019-04-24 09:40:00.0","marketAverage":"208.13","marketVolume":"65573","marketNumberOfTrades":"277"},
                                                                        {"date":"2019-04-24 09:41:00.0","marketAverage":"207.909","marketVolume":"145579","marketNumberOfTrades":"837"},
                                                                        {"date":"2019-04-24 09:42:00.0","marketAverage":"207.976","marketVolume":"75275","marketNumberOfTrades":"317"},
                                                                        {"date":"2019-04-24 09:43:00.0","marketAverage":"208.051","marketVolume":"73379","marketNumberOfTrades":"268"},
                                                                        {"date":"2019-04-24 09:44:00.0","marketAverage":"207.966","marketVolume":"58743","marketNumberOfTrades":"277"},
                                                                        {"date":"2019-04-24 09:45:00.0","marketAverage":"207.846","marketVolume":"93644","marketNumberOfTrades":"452"},
                                                                        {"date":"2019-04-24 09:46:00.0","marketAverage":"207.783","marketVolume":"86574","marketNumberOfTrades":"495"},
                                                                        {"date":"2019-04-24 09:47:00.0","marketAverage":"207.883","marketVolume":"70293","marketNumberOfTrades":"404"},
                                                                        {"date":"2019-04-24 09:48:00.0","marketAverage":"208.025","marketVolume":"80883","marketNumberOfTrades":"383"},
                                                                        {"date":"2019-04-24 09:49:00.0","marketAverage":"207.95","marketVolume":"42638","marketNumberOfTrades":"216"},
                                                                        {"date":"2019-04-24 09:50:00.0","marketAverage":"207.568","marketVolume":"196327","marketNumberOfTrades":"962"},
                                                                        {"date":"2019-04-24 09:51:00.0","marketAverage":"207.353","marketVolume":"105278","marketNumberOfTrades":"505"},
                                                                        {"date":"2019-04-24 09:52:00.0","marketAverage":"207.452","marketVolume":"103388","marketNumberOfTrades":"516"},
                                                                        {"date":"2019-04-24 09:53:00.0","marketAverage":"207.514","marketVolume":"66257","marketNumberOfTrades":"378"},
                                                                        {"date":"2019-04-24 09:54:00.0","marketAverage":"207.441","marketVolume":"66070","marketNumberOfTrades":"342"},
                                                			{"date":"2019-04-24 09:55:00.0","marketAverage":"207.526","marketVolume":"76781","marketNumberOfTrades":"356"},
                                                			{"date":"2019-04-24 09:56:00.0","marketAverage":"207.567","marketVolume":"66121","marketNumberOfTrades":"329"},
                                                			{"date":"2019-04-24 09:57:00.0","marketAverage":"207.496","marketVolume":"34244","marketNumberOfTrades":"196"},
                                                			{"date":"2019-04-24 09:58:00.0","marketAverage":"207.431","marketVolume":"69410","marketNumberOfTrades":"361"},
                                                			{"date":"2019-04-24 09:59:00.0","marketAverage":"207.383","marketVolume":"120687","marketNumberOfTrades":"465"},
                                                			{"date":"2019-04-24 10:00:00.0","marketAverage":"207.21","marketVolume":"122108","marketNumberOfTrades":"508"},
                                                			{"date":"2019-04-24 10:01:00.0","marketAverage":"207.268","marketVolume":"59865","marketNumberOfTrades":"312"},
                                                			{"date":"2019-04-24 10:02:00.0","marketAverage":"207.425","marketVolume":"64791","marketNumberOfTrades":"338"},
                                                			{"date":"2019-04-24 10:03:00.0","marketAverage":"207.405","marketVolume":"52492","marketNumberOfTrades":"299"},
                                                			{"date":"2019-04-24 10:04:00.0","marketAverage":"207.387","marketVolume":"40883","marketNumberOfTrades":"230"},
                                                			{"date":"2019-04-24 10:05:00.0","marketAverage":"207.534","marketVolume":"67392","marketNumberOfTrades":"331"},
                                                			{"date":"2019-04-24 10:06:00.0","marketAverage":"207.485","marketVolume":"36159","marketNumberOfTrades":"234"},
                                                			{"date":"2019-04-24 10:07:00.0","marketAverage":"207.534","marketVolume":"46362","marketNumberOfTrades":"305"},
                                                			{"date":"2019-04-24 10:08:00.0","marketAverage":"207.524","marketVolume":"40782","marketNumberOfTrades":"206"},
                                                			{"date":"2019-04-24 10:09:00.0","marketAverage":"207.535","marketVolume":"74714","marketNumberOfTrades":"273"},
                                                			{"date":"2019-04-24 10:10:00.0","marketAverage":"207.635","marketVolume":"32163","marketNumberOfTrades":"206"},
                                                			{"date":"2019-04-24 10:11:00.0","marketAverage":"207.591","marketVolume":"22738","marketNumberOfTrades":"160"},
                                                			{"date":"2019-04-24 10:12:00.0","marketAverage":"207.654","marketVolume":"30425","marketNumberOfTrades":"149"},
                                                			{"date":"2019-04-24 10:13:00.0","marketAverage":"207.726","marketVolume":"70042","marketNumberOfTrades":"271"},
                                                			{"date":"2019-04-24 10:14:00.0","marketAverage":"207.799","marketVolume":"36375","marketNumberOfTrades":"182"},
                                                			{"date":"2019-04-24 10:15:00.0","marketAverage":"207.775","marketVolume":"49433","marketNumberOfTrades":"306"},
                                                			{"date":"2019-04-24 10:16:00.0","marketAverage":"207.873","marketVolume":"44573","marketNumberOfTrades":"223"},
                                                			{"date":"2019-04-24 10:17:00.0","marketAverage":"207.98","marketVolume":"94313","marketNumberOfTrades":"390"},
                                                			{"date":"2019-04-24 10:18:00.0","marketAverage":"208.138","marketVolume":"55893","marketNumberOfTrades":"284"},
                                                			{"date":"2019-04-24 10:19:00.0","marketAverage":"208.135","marketVolume":"68022","marketNumberOfTrades":"368"},
                                                			{"date":"2019-04-24 10:20:00.0","marketAverage":"208.109","marketVolume":"39713","marketNumberOfTrades":"255"},
                                                			{"date":"2019-04-24 10:21:00.0","marketAverage":"208.055","marketVolume":"29665","marketNumberOfTrades":"169"},
                                                			{"date":"2019-04-24 12:24:00.0","marketAverage":"207.732","marketVolume":"15526","marketNumberOfTrades":"116"},
                                                			{"date":"2019-04-24 12:25:00.0","marketAverage":"207.717","marketVolume":"11439","marketNumberOfTrades":"88"},
                                                			{"date":"2019-04-24 12:40:00.0","marketAverage":"207.5","marketVolume":"46770","marketNumberOfTrades":"201"},
                                                			{"date":"2019-04-24 12:41:00.0","marketAverage":"207.557","marketVolume":"16255","marketNumberOfTrades":"86"},
                                                			{"date":"2019-04-24 12:42:00.0","marketAverage":"207.549","marketVolume":"8519","marketNumberOfTrades":"55"},
                                                			{"date":"2019-04-24 12:43:00.0","marketAverage":"207.528","marketVolume":"5484","marketNumberOfTrades":"45"},
                                                			{"date":"2019-04-24 12:44:00.0","marketAverage":"207.466","marketVolume":"57823","marketNumberOfTrades":"235"},
                                                			{"date":"2019-04-24 12:45:00.0","marketAverage":"207.388","marketVolume":"30522","marketNumberOfTrades":"135"},
                                                			{"date":"2019-04-24 12:46:00.0","marketAverage":"207.36","marketVolume":"15436","marketNumberOfTrades":"75"},
                                                			{"date":"2019-04-24 12:47:00.0","marketAverage":"207.398","marketVolume":"14550","marketNumberOfTrades":"46"},
                                                			{"date":"2019-04-24 10:22:00.0","marketAverage":"208.115","marketVolume":"42440","marketNumberOfTrades":"227"},
                                                			{"date":"2019-04-24 10:23:00.0","marketAverage":"208.13","marketVolume":"37893","marketNumberOfTrades":"209"},
                                                			{"date":"2019-04-24 10:24:00.0","marketAverage":"208.112","marketVolume":"39399","marketNumberOfTrades":"173"},
                                                			{"date":"2019-04-24 10:25:00.0","marketAverage":"208.091","marketVolume":"19315","marketNumberOfTrades":"122"},
                                                			{"date":"2019-04-24 10:26:00.0","marketAverage":"208.122","marketVolume":"26787","marketNumberOfTrades":"176"},
                                                			{"date":"2019-04-24 10:27:00.0","marketAverage":"208.09","marketVolume":"25890","marketNumberOfTrades":"166"},
                                                			{"date":"2019-04-24 10:28:00.0","marketAverage":"208.1","marketVolume":"15258","marketNumberOfTrades":"110"},
                                                			{"date":"2019-04-24 10:29:00.0","marketAverage":"208.079","marketVolume":"27031","marketNumberOfTrades":"163"},
                                                			{"date":"2019-04-24 10:30:00.0","marketAverage":"207.986","marketVolume":"63770","marketNumberOfTrades":"400"},
                                                			{"date":"2019-04-24 10:31:00.0","marketAverage":"207.857","marketVolume":"24546","marketNumberOfTrades":"142"},
                                                			{"date":"2019-04-24 10:32:00.0","marketAverage":"207.828","marketVolume":"27960","marketNumberOfTrades":"138"},
                                                			{"date":"2019-04-24 10:33:00.0","marketAverage":"207.912","marketVolume":"29792","marketNumberOfTrades":"199"},
                                                			{"date":"2019-04-24 10:34:00.0","marketAverage":"207.949","marketVolume":"24202","marketNumberOfTrades":"143"},
                                                			{"date":"2019-04-24 10:35:00.0","marketAverage":"208.005","marketVolume":"46155","marketNumberOfTrades":"243"},
                                                			{"date":"2019-04-24 10:36:00.0","marketAverage":"208.03","marketVolume":"31063","marketNumberOfTrades":"211"},
                                                			{"date":"2019-04-24 10:37:00.0","marketAverage":"208.048","marketVolume":"15684","marketNumberOfTrades":"110"},
                                                			{"date":"2019-04-24 10:38:00.0","marketAverage":"207.996","marketVolume":"41136","marketNumberOfTrades":"194"},
                                                			{"date":"2019-04-24 10:39:00.0","marketAverage":"208","marketVolume":"12800","marketNumberOfTrades":"93"},
                                                			{"date":"2019-04-24 10:40:00.0","marketAverage":"207.945","marketVolume":"25990","marketNumberOfTrades":"137"},
                                                			{"date":"2019-04-24 10:41:00.0","marketAverage":"207.938","marketVolume":"16960","marketNumberOfTrades":"88"},
                                                			{"date":"2019-04-24 10:42:00.0","marketAverage":"207.905","marketVolume":"23562","marketNumberOfTrades":"146"},
                                                			{"date":"2019-04-24 10:43:00.0","marketAverage":"207.848","marketVolume":"36533","marketNumberOfTrades":"242"},
                                                			{"date":"2019-04-24 10:44:00.0","marketAverage":"207.755","marketVolume":"43728","marketNumberOfTrades":"223"},
                                                			{"date":"2019-04-24 10:45:00.0","marketAverage":"207.717","marketVolume":"40238","marketNumberOfTrades":"170"},
                                                			{"date":"2019-04-24 10:46:00.0","marketAverage":"207.794","marketVolume":"15793","marketNumberOfTrades":"113"},
                                                			{"date":"2019-04-24 10:47:00.0","marketAverage":"207.796","marketVolume":"29708","marketNumberOfTrades":"136"},
                                                			{"date":"2019-04-24 10:48:00.0","marketAverage":"207.778","marketVolume":"26040","marketNumberOfTrades":"98"},
                                                			{"date":"2019-04-24 10:49:00.0","marketAverage":"207.785","marketVolume":"25505","marketNumberOfTrades":"114"},
                                                			{"date":"2019-04-24 10:50:00.0","marketAverage":"207.83","marketVolume":"22061","marketNumberOfTrades":"108"},
                                                			{"date":"2019-04-24 10:51:00.0","marketAverage":"207.856","marketVolume":"32274","marketNumberOfTrades":"132"},
                                                			{"date":"2019-04-24 10:52:00.0","marketAverage":"207.792","marketVolume":"20665","marketNumberOfTrades":"125"},
                                                			{"date":"2019-04-24 10:53:00.0","marketAverage":"207.859","marketVolume":"12486","marketNumberOfTrades":"88"},
                                                			{"date":"2019-04-24 10:54:00.0","marketAverage":"207.899","marketVolume":"47839","marketNumberOfTrades":"233"},
                                                			{"date":"2019-04-24 10:55:00.0","marketAverage":"207.869","marketVolume":"16648","marketNumberOfTrades":"97"},
                                                			{"date":"2019-04-24 10:56:00.0","marketAverage":"207.944","marketVolume":"18239","marketNumberOfTrades":"112"},
                                                			{"date":"2019-04-24 10:57:00.0","marketAverage":"207.99","marketVolume":"32444","marketNumberOfTrades":"156"},
                                                			{"date":"2019-04-24 10:58:00.0","marketAverage":"208.095","marketVolume":"36430","marketNumberOfTrades":"175"},
                                                			{"date":"2019-04-24 10:59:00.0","marketAverage":"208.145","marketVolume":"40603","marketNumberOfTrades":"178"},
                                                			{"date":"2019-04-24 11:00:00.0","marketAverage":"208.169","marketVolume":"37897","marketNumberOfTrades":"184"},
                                                			{"date":"2019-04-24 11:01:00.0","marketAverage":"208.199","marketVolume":"26527","marketNumberOfTrades":"116"},
                                                			{"date":"2019-04-24 11:02:00.0","marketAverage":"208.216","marketVolume":"38702","marketNumberOfTrades":"200"},
                                                			{"date":"2019-04-24 11:03:00.0","marketAverage":"208.105","marketVolume":"25621","marketNumberOfTrades":"145"},
                                                			{"date":"2019-04-24 11:04:00.0","marketAverage":"208.129","marketVolume":"20730","marketNumberOfTrades":"123"},
                                                			{"date":"2019-04-24 11:05:00.0","marketAverage":"208.169","marketVolume":"19869","marketNumberOfTrades":"140"},
                                                			{"date":"2019-04-24 11:06:00.0","marketAverage":"208.187","marketVolume":"24361","marketNumberOfTrades":"115"},
                                                			{"date":"2019-04-24 11:07:00.0","marketAverage":"208.226","marketVolume":"29238","marketNumberOfTrades":"139"},
                                                			{"date":"2019-04-24 11:08:00.0","marketAverage":"208.274","marketVolume":"38016","marketNumberOfTrades":"212"},
                                                			{"date":"2019-04-24 11:09:00.0","marketAverage":"208.305","marketVolume":"23858","marketNumberOfTrades":"120"},
                                                			{"date":"2019-04-24 11:10:00.0","marketAverage":"208.339","marketVolume":"33577","marketNumberOfTrades":"173"},
                                                			{"date":"2019-04-24 11:11:00.0","marketAverage":"208.305","marketVolume":"20275","marketNumberOfTrades":"107"},
                                                			{"date":"2019-04-24 11:12:00.0","marketAverage":"208.328","marketVolume":"23036","marketNumberOfTrades":"110"},
                                                			{"date":"2019-04-24 11:13:00.0","marketAverage":"208.399","marketVolume":"34823","marketNumberOfTrades":"178"},
                                                			{"date":"2019-04-24 11:14:00.0","marketAverage":"208.448","marketVolume":"38865","marketNumberOfTrades":"186"},
                                                			{"date":"2019-04-24 11:15:00.0","marketAverage":"208.412","marketVolume":"35514","marketNumberOfTrades":"196"},
                                                			{"date":"2019-04-24 11:16:00.0","marketAverage":"208.335","marketVolume":"35469","marketNumberOfTrades":"203"},
                                                			{"date":"2019-04-24 11:17:00.0","marketAverage":"208.357","marketVolume":"40190","marketNumberOfTrades":"206"},
                                                			{"date":"2019-04-24 11:18:00.0","marketAverage":"208.34","marketVolume":"35678","marketNumberOfTrades":"163"},
                                                			{"date":"2019-04-24 11:19:00.0","marketAverage":"208.333","marketVolume":"32778","marketNumberOfTrades":"144"},
                                                			{"date":"2019-04-24 11:20:00.0","marketAverage":"208.252","marketVolume":"51761","marketNumberOfTrades":"250"},
                                                			{"date":"2019-04-24 11:21:00.0","marketAverage":"208.207","marketVolume":"16349","marketNumberOfTrades":"105"},
                                                			{"date":"2019-04-24 11:22:00.0","marketAverage":"208.194","marketVolume":"21699","marketNumberOfTrades":"133"},
                                                			{"date":"2019-04-24 11:23:00.0","marketAverage":"208.132","marketVolume":"22329","marketNumberOfTrades":"136"},
                                                			{"date":"2019-04-24 11:24:00.0","marketAverage":"208.122","marketVolume":"25197","marketNumberOfTrades":"112"},
                                                			{"date":"2019-04-24 11:25:00.0","marketAverage":"208.101","marketVolume":"31780","marketNumberOfTrades":"131"},
                                                			{"date":"2019-04-24 11:26:00.0","marketAverage":"208.154","marketVolume":"15436","marketNumberOfTrades":"86"},
                                                			{"date":"2019-04-24 11:27:00.0","marketAverage":"208.127","marketVolume":"22799","marketNumberOfTrades":"97"},
                                                			{"date":"2019-04-24 11:28:00.0","marketAverage":"207.945","marketVolume":"49126","marketNumberOfTrades":"195"},
                                                			{"date":"2019-04-24 11:29:00.0","marketAverage":"207.944","marketVolume":"28897","marketNumberOfTrades":"134"},
                                                			{"date":"2019-04-24 11:30:00.0","marketAverage":"207.939","marketVolume":"15299","marketNumberOfTrades":"108"},
                                                			{"date":"2019-04-24 11:31:00.0","marketAverage":"207.943","marketVolume":"12826","marketNumberOfTrades":"87"},
                                                			{"date":"2019-04-24 11:32:00.0","marketAverage":"207.973","marketVolume":"12712","marketNumberOfTrades":"79"},
                                                			{"date":"2019-04-24 11:33:00.0","marketAverage":"207.997","marketVolume":"17536","marketNumberOfTrades":"101"},
                                                			{"date":"2019-04-24 11:34:00.0","marketAverage":"208.085","marketVolume":"21028","marketNumberOfTrades":"92"},
                                                			{"date":"2019-04-24 11:35:00.0","marketAverage":"208.253","marketVolume":"47207","marketNumberOfTrades":"250"},
                                                			{"date":"2019-04-24 11:36:00.0","marketAverage":"208.239","marketVolume":"21817","marketNumberOfTrades":"107"},
                                                			{"date":"2019-04-24 11:37:00.0","marketAverage":"208.225","marketVolume":"19370","marketNumberOfTrades":"116"},
                                                			{"date":"2019-04-24 11:38:00.0","marketAverage":"208.231","marketVolume":"18183","marketNumberOfTrades":"102"},
                                                			{"date":"2019-04-24 11:39:00.0","marketAverage":"208.178","marketVolume":"48870","marketNumberOfTrades":"219"},
                                                			{"date":"2019-04-24 11:40:00.0","marketAverage":"208.134","marketVolume":"39039","marketNumberOfTrades":"178"},
                                                			{"date":"2019-04-24 11:41:00.0","marketAverage":"208.041","marketVolume":"37725","marketNumberOfTrades":"251"},
                                                			{"date":"2019-04-24 11:42:00.0","marketAverage":"207.956","marketVolume":"17919","marketNumberOfTrades":"90"},
                                                			{"date":"2019-04-24 11:43:00.0","marketAverage":"208.003","marketVolume":"21044","marketNumberOfTrades":"71"},
                                                			{"date":"2019-04-24 11:44:00.0","marketAverage":"207.996","marketVolume":"22318","marketNumberOfTrades":"108"},
                                                			{"date":"2019-04-24 11:45:00.0","marketAverage":"207.95","marketVolume":"20848","marketNumberOfTrades":"96"},
                                                			{"date":"2019-04-24 11:46:00.0","marketAverage":"207.898","marketVolume":"23195","marketNumberOfTrades":"108"},
                                                			{"date":"2019-04-24 11:47:00.0","marketAverage":"207.801","marketVolume":"38941","marketNumberOfTrades":"154"},
                                                			{"date":"2019-04-24 11:48:00.0","marketAverage":"207.849","marketVolume":"18681","marketNumberOfTrades":"72"},
                                                			{"date":"2019-04-24 11:49:00.0","marketAverage":"207.853","marketVolume":"25446","marketNumberOfTrades":"105"},
                                                			{"date":"2019-04-24 11:50:00.0","marketAverage":"207.823","marketVolume":"22402","marketNumberOfTrades":"112"},
                                                			{"date":"2019-04-24 11:51:00.0","marketAverage":"207.628","marketVolume":"90600","marketNumberOfTrades":"328"},
                                                			{"date":"2019-04-24 11:52:00.0","marketAverage":"207.581","marketVolume":"47557","marketNumberOfTrades":"175"},
                                                			{"date":"2019-04-24 12:12:00.0","marketAverage":"207.752","marketVolume":"23162","marketNumberOfTrades":"135"},
                                                			{"date":"2019-04-24 11:53:00.0","marketAverage":"207.738","marketVolume":"52353","marketNumberOfTrades":"195"},
                                                			{"date":"2019-04-24 11:54:00.0","marketAverage":"207.824","marketVolume":"34352","marketNumberOfTrades":"154"},
                                                			{"date":"2019-04-24 11:55:00.0","marketAverage":"207.842","marketVolume":"30036","marketNumberOfTrades":"162"},
                                                			{"date":"2019-04-24 11:56:00.0","marketAverage":"207.797","marketVolume":"18265","marketNumberOfTrades":"90"},
                                                			{"date":"2019-04-24 11:57:00.0","marketAverage":"207.795","marketVolume":"16154","marketNumberOfTrades":"86"},
                                                			{"date":"2019-04-24 11:58:00.0","marketAverage":"207.844","marketVolume":"11945","marketNumberOfTrades":"60"},
                                                			{"date":"2019-04-24 11:59:00.0","marketAverage":"207.824","marketVolume":"20208","marketNumberOfTrades":"96"},
                                                			{"date":"2019-04-24 12:00:00.0","marketAverage":"207.818","marketVolume":"9556","marketNumberOfTrades":"44"},
                                                			{"date":"2019-04-24 12:01:00.0","marketAverage":"207.768","marketVolume":"30938","marketNumberOfTrades":"144"},
                                                			{"date":"2019-04-24 12:02:00.0","marketAverage":"207.734","marketVolume":"22388","marketNumberOfTrades":"127"},
                                                			{"date":"2019-04-24 12:03:00.0","marketAverage":"207.651","marketVolume":"23121","marketNumberOfTrades":"89"},
                                                			{"date":"2019-04-24 12:04:00.0","marketAverage":"207.619","marketVolume":"27352","marketNumberOfTrades":"94"},
                                                			{"date":"2019-04-24 12:05:00.0","marketAverage":"207.649","marketVolume":"26565","marketNumberOfTrades":"73"},
                                                			{"date":"2019-04-24 12:06:00.0","marketAverage":"207.618","marketVolume":"8579","marketNumberOfTrades":"42"},
                                                			{"date":"2019-04-24 12:07:00.0","marketAverage":"207.609","marketVolume":"17457","marketNumberOfTrades":"92"},
                                                			{"date":"2019-04-24 12:08:00.0","marketAverage":"207.596","marketVolume":"18589","marketNumberOfTrades":"71"},
                                                			{"date":"2019-04-24 12:09:00.0","marketAverage":"207.568","marketVolume":"14252","marketNumberOfTrades":"82"},
                                                			{"date":"2019-04-24 12:10:00.0","marketAverage":"207.615","marketVolume":"33148","marketNumberOfTrades":"139"},
                                                			{"date":"2019-04-24 12:11:00.0","marketAverage":"207.705","marketVolume":"35040","marketNumberOfTrades":"165"},
                                                			{"date":"2019-04-24 12:13:00.0","marketAverage":"207.666","marketVolume":"8581","marketNumberOfTrades":"48"},
                                                			{"date":"2019-04-24 12:14:00.0","marketAverage":"207.641","marketVolume":"11469","marketNumberOfTrades":"69"},
                                                			{"date":"2019-04-24 12:15:00.0","marketAverage":"207.691","marketVolume":"11604","marketNumberOfTrades":"74"},
                                                			{"date":"2019-04-24 12:16:00.0","marketAverage":"207.679","marketVolume":"9353","marketNumberOfTrades":"69"},
                                                			{"date":"2019-04-24 12:17:00.0","marketAverage":"207.673","marketVolume":"16302","marketNumberOfTrades":"101"},
                                                			{"date":"2019-04-24 12:18:00.0","marketAverage":"207.618","marketVolume":"8531","marketNumberOfTrades":"54"},
                                                			{"date":"2019-04-24 12:19:00.0","marketAverage":"207.658","marketVolume":"17930","marketNumberOfTrades":"122"},
                                                			{"date":"2019-04-24 12:20:00.0","marketAverage":"207.64","marketVolume":"8643","marketNumberOfTrades":"66"},
                                                			{"date":"2019-04-24 12:21:00.0","marketAverage":"207.687","marketVolume":"12149","marketNumberOfTrades":"96"},
                                                			{"date":"2019-04-24 12:22:00.0","marketAverage":"207.743","marketVolume":"10010","marketNumberOfTrades":"60"},
                                                			{"date":"2019-04-24 12:23:00.0","marketAverage":"207.787","marketVolume":"17409","marketNumberOfTrades":"102"},
                                                			{"date":"2019-04-24 12:26:00.0","marketAverage":"207.66","marketVolume":"14921","marketNumberOfTrades":"83"},
                                                			{"date":"2019-04-24 12:27:00.0","marketAverage":"207.665","marketVolume":"16074","marketNumberOfTrades":"114"},
                                                			{"date":"2019-04-24 12:28:00.0","marketAverage":"207.701","marketVolume":"14637","marketNumberOfTrades":"107"},
                                                			{"date":"2019-04-24 12:29:00.0","marketAverage":"207.663","marketVolume":"8731","marketNumberOfTrades":"68"},
                                                			{"date":"2019-04-24 12:30:00.0","marketAverage":"207.664","marketVolume":"16673","marketNumberOfTrades":"123"},
                                                			{"date":"2019-04-24 12:31:00.0","marketAverage":"207.617","marketVolume":"14966","marketNumberOfTrades":"95"},
                                                			{"date":"2019-04-24 12:32:00.0","marketAverage":"207.638","marketVolume":"7155","marketNumberOfTrades":"50"},
                                                			{"date":"2019-04-24 12:33:00.0","marketAverage":"207.606","marketVolume":"15485","marketNumberOfTrades":"101"},
                                                			{"date":"2019-04-24 12:34:00.0","marketAverage":"207.584","marketVolume":"16206","marketNumberOfTrades":"94"},
                                                			{"date":"2019-04-24 12:35:00.0","marketAverage":"207.554","marketVolume":"22522","marketNumberOfTrades":"110"},
                                                			{"date":"2019-04-24 12:36:00.0","marketAverage":"207.591","marketVolume":"26823","marketNumberOfTrades":"178"},
                                                			{"date":"2019-04-24 12:37:00.0","marketAverage":"207.616","marketVolume":"19142","marketNumberOfTrades":"95"},
                                                			{"date":"2019-04-24 12:38:00.0","marketAverage":"207.546","marketVolume":"15797","marketNumberOfTrades":"65"},
                                                			{"date":"2019-04-24 12:39:00.0","marketAverage":"207.524","marketVolume":"20528","marketNumberOfTrades":"75"},
                                                			{"date":"2019-04-24 12:48:00.0","marketAverage":"207.457","marketVolume":"21678","marketNumberOfTrades":"103"},
                                                			{"date":"2019-04-24 12:49:00.0","marketAverage":"207.484","marketVolume":"17025","marketNumberOfTrades":"81"},
                                                			{"date":"2019-04-24 12:50:00.0","marketAverage":"207.507","marketVolume":"11399","marketNumberOfTrades":"75"},
                                                			{"date":"2019-04-24 12:51:00.0","marketAverage":"207.494","marketVolume":"8465","marketNumberOfTrades":"51"},
                                                			{"date":"2019-04-24 12:52:00.0","marketAverage":"207.475","marketVolume":"26341","marketNumberOfTrades":"183"},
                                                			{"date":"2019-04-24 12:53:00.0","marketAverage":"207.512","marketVolume":"19592","marketNumberOfTrades":"101"},
                                                			{"date":"2019-04-24 12:54:00.0","marketAverage":"207.571","marketVolume":"10204","marketNumberOfTrades":"59"},
                                                			{"date":"2019-04-24 12:55:00.0","marketAverage":"207.528","marketVolume":"27267","marketNumberOfTrades":"105"},
                                                			{"date":"2019-04-24 12:56:00.0","marketAverage":"207.465","marketVolume":"24538","marketNumberOfTrades":"150"},
                                                			{"date":"2019-04-24 12:57:00.0","marketAverage":"207.463","marketVolume":"16709","marketNumberOfTrades":"125"},
                                                			{"date":"2019-04-24 12:58:00.0","marketAverage":"207.365","marketVolume":"36486","marketNumberOfTrades":"167"},
                                                			{"date":"2019-04-24 12:59:00.0","marketAverage":"207.307","marketVolume":"24471","marketNumberOfTrades":"111"}];
                                                        }

                                                        function generateData(tbl) {
                                                                var arr = [];

                                                                for (var i = 0; i < tbl.length; i++) {
                                                                        if (tbl[i]['date'].substr(14,2) % 1 == 0) {
                                                                                arr.push(tbl[i]['marketAverage']);
                                                                        }
                                                                }

                                                                return arr;
                                                        }

                                                        function generateLabels(tbl) {
                                                                var arr = [];

                                                                for (var i = 0; i < tbl.length; i++) {
                                                                        if (tbl[i]['date'].substr(14,2) % 1 == 0) {
                                                                                arr.push(tbl[i]['date'].substr(11,5));
                                                                        }
                                                                }

                                                                return arr;
                                                        }

                                                        var tbl = generateTable();

                                                        var data = {
                                                                labels: generateLabels(tbl),
                                                                datasets: [{
                                                                        data: generateData(tbl),
                                                                        hidden: false
                                                                }]
                                                        };

                                                        var options = {
                                                                maintainAspectRatio: false,
                                                                spanGaps: false,
                                                                legend: {
                                                                        display: false
                                                                },
                                                                elements: {
                                                                        line: {
                                                                                tension: 0
                                                                        }
                                                                }
                                                        };

                                                        var chart = new Chart(ctx, {
                                                                type: 'line',
                                                                data: data,
                                                                options: options
                                                        });
                                                        </script>
                                                </div>
                                        <h3 class="text-center">About</h3>
                                        <p id="ticker_about">More info about a stock goes here if available</p>
                                </div>
                                <div class="col-lg-3" id="stock_table_div">
                                        <a href="#">Add to Watchlist</a><p></p>
                                        <table class = "table stock_tables">
                                                <!-- Portfolio stock details -->
                                           <tbody>
                                              <th colspan="2">
                                                 <b>Add to porfolio</b>
                                              </th>
                                              <tr>
                                                 <td>Shares</th>
                                                 <td> SHARE COUNT</th>
                                              </tr>
                                              <tr>
                                                 <td> Date Purchased </td>
                                                 <td> Date </td>
                                              </tr>
                                              <tr>
                                                 <td> Time Purchased </td>
                                                 <td> TIME </td>
                                              </tr>
                                              <tr>
                                                 <td colspan="2" align="center">
                                                         <a href="#">Add</a>
                                                 </td>
                                           </tbody>
                                        </table>

                                        <!-- Statistics -->
                                        <table class = "table stock_tables">

                                           <tbody>
                                              <tr>
                                                 <th colspan="2">
                                                         <b>Statistics</b>
                                                 </th>
                                              </tr>
                                              <tr>
                                                 <td>Previous Close</td>
                                                 <td> Value</td>
                                              </tr>
                                              <tr>
                                                 <td>Open</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>High</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>Low</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>52 Wk. High</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>52 Wk. Low</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>Volume</td>
                                                 <td>Value</td>
                                              </tr>
                                              <tr>
                                                 <td>Avg. Volume</td>
                                                 <td>Value</td>
                                              </tr>
                                           </tbody>

                                        </table>
                                </div>
                                <div class="col-lg-2">
                                </div>
                        </div>

                        <div class="offset-lg-2">
                                <div class="row">
                                        <div class="mx-auto col-lg-6">
                                                <h3 class="text-center">Relevant articles</h3>
                                        </div>
                                        <div class="col-lg-2 col-md-2 col-sm-2">
                                        </div>
                                </div>
                                <div class="row">
                                        <div class="col-lg-2 col-md-2 col-sm-2 my-auto">
                                                <a href="#" class="bg-mute round arrow">Previous &laquo;</a>
                                        </div>
                                        <div class="col-lg-6 col-md-6 col-sm-6">

                                                <!-- First Article -->
                                                <a href="#" class="nounderline">
                                                <div class="card mb-3" style="max-width: 100%;">
                                                  <div class="row no-gutters hoverable">
                                                    <div class="col-md-4">
                                                      <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="hoverable card-img" alt="Image Unavailable">
                                                    </div>
                                                    <div class="col-md-8">
                                                      <div class="card-body">
                                                        <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                        <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                                      </div>
                                                    </div>
                                                  </div>
                                                </div>
                                                </a>

                                                <!-- Second Article -->
                                                <a href="#" class="nounderline">
                                                <div class="card mb-3" style="max-width: 100%;">
                                                  <div class="row no-gutters hoverable">
                                                    <div class="col-md-4">
                                                      <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="hoverable card-img" alt="Image Unavailable">
                                                    </div>
                                                    <div class="col-md-8">
                                                      <div class="card-body">
                                                        <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                        <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                                      </div>
                                                    </div>
                                                  </div>
                                                </div>
                                                </a>

                                                <!-- Third Article -->
                                                <a href="#" class="nounderline">
                                                <div class="card mb-3" style="max-width: 100%;">
                                                  <div class="row no-gutters hoverable">
                                                    <div class="col-md-4">
                                                      <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="hoverable card-img" alt="Image Unavailable">
                                                    </div>
                                                    <div class="col-md-8">
                                                      <div class="card-body">
                                                        <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                        <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                                      </div>
                                                    </div>
                                                  </div>
                                                </div>
                                                </a>

                                                <!-- Fourth Article -->
                                                <a href="#" class="nounderline">
                                                <div class="card mb-3" style="max-width: 100%;">
                                                  <div class="row no-gutters hoverable">
                                                    <div class="col-md-4">
                                                      <img src="https://ei.marketwatch.com/Multimedia/2019/04/17/Photos/ZH/MW-HH786_model3_20190417122228_ZH.jpg?uuid=fe15c706-612c-11e9-b5b6-9c8e992d421e" class="hoverable card-img" alt="Image Unavailable">
                                                    </div>
                                                    <div class="col-md-8">
                                                      <div class="card-body">
                                                        <h5 class="card-title"> Tesla Can't Stop Dreaming Big</h5>
                                                        <p class="card-text">Elon Musk’s ambitions to turn Tesla into a dominant automobile player have become a liability instead of an asset.</p>
                                                      </div>
                                                    </div>
                                                  </div>
                                                </div>
                                                </a>

                                        </div>
                                        <div class="col-lg-2 col-md-2 col-sm-2 my-auto">
                                                <a href="#" class="bg-mute round float-right arrow"> Next &raquo;</a>
                                        </div>
                                </div>

                        </div>

        </div>


<div class="footer_div">
    <?php include "footer.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>
