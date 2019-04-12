<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="global_assets/portfolio.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>

        <div class="header_div navbar-spacers">
            <?php include "header.php"; ?>
            <!-- Use jquery to insert header content here if PHP fails    -->
        </div>

<!-- Body of portfolio -->
<div class="">
        <div class="row">
                <div class="offset-lg-1 col-lg-10">
                        <!-- Table goes here -->
                        <table class = "table stock_tables">

                           <tbody>
                              <tr>
                                         <td> Sym </td>
                                         <td> Price </td>
                                         <td> Chg </td>
                                         <td> Chg % </td>
                                         <td> Chg $ </td>
                                         <td> Vol </td>
                                         <td> Avg Vol </td>
                                         <td> Shrs </td>
                                         <td> $/Shrs </td>
                                         <td> Cost Basis </td>
                                         <td> Mkt Val </td>
                                         <td> Tot Chg $ </td>
                                         <td> Tot Chg % </td>
                                         <td> Day Rng </td>
                                         <td> YTD Chg % </td>
                              </tr>
                              <tr>
                                      <td class="" id="Sym"> <a href="#">AMZN</a> </td>
                                      <td class="positive" id="Price"> $1783.76 </td>
                                      <td class="positive" id="Chg"> +9.50 </td>
                                      <td class="positive" id="Chg %"> +0.53% </td>
                                      <td class="positive" id="Chg $"> +2850.00 </td>
                                      <td id="Vol"> 4,632,933 </td>
                                      <td id="Avg Vol"> 4,368,875 </td>
                                      <td id="Shrs"> 300</td>
                                      <td id="$/Shrs"> 327.37 </td>
                                      <td id="Cost Basis"> 98,210 </td>
                                      <td id="Mkt Val"> 535,128 </td>
                                      <td class="positive" id="Tot Chg $"> +436,918 </td>
                                      <td class="positive" id="Tot Chg %"> +444.88% </td>
                                      <td id="Day Rng"> 1773.36 - 1805.77 </td>
                                      <td id="YTD Chg %"> 16.43% </td>
                              </tr>
                              <tr>
                                      <td class="" id="Sym"> <a href="#">GOOGL</a> </td>
                                      <td class="negative" id="Price"> $1189.84 </td>
                                      <td class="negative" id="Chg"> -7.54 </td>
                                      <td class="negative" id="Chg %"> -0.63% </td>
                                      <td class="negative" id="Chg $"> -3016.00 </td>
                                      <td id="Vol"> 1,497,895 </td>
                                      <td id="Avg Vol"> 1,499,716 </td>
                                      <td id="Shrs"> 400</td>
                                      <td id="$/Shrs"> 605.14 </td>
                                      <td id="Cost Basis"> 242,056 </td>
                                      <td id="Mkt Val"> 475,935 </td>
                                      <td class="positive" id="Tot Chg $"> +233,879 </td>
                                      <td class="positive" id="Tot Chg %"> +96.62% </td>
                                      <td id="Day Rng"> 1181.76 - 1207.65 </td>
                                      <td id="YTD Chg %"> 12.19% </td>
                              </tr>
                              <tr>
                                      <td id="Sym"> Total </td>
                                      <td id="Price"> - </td>
                                      <td id="Chg"> - </td>
                                      <td id="Chg %"> - </td>
                                      <td id="Chg $"> - </td>
                                      <td id="Vol"> - </td>
                                      <td id="Avg Vol"> - </td>
                                      <td id="Shrs"> - </td>
                                      <td id="$/Shrs"> - </td>
                                      <td id="Cost Basis"> - </td>
                                      <td id="Mkt Val"> - </td>
                                      <td id="Tot Chg $"> - </td>
                                      <td id="Tot Chg %"> - </td>
                                      <td id="Day Rng"> - </td>
                                      <td id="YTD Chg %"> - </td>
                              </tr>
                           </tbody>

                        </table>
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
