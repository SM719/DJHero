<?php
$artist = $_GET["artist"];
$album = $_GET["album"];
$track= $_GET["track"];

$track = str_replace(' ', '+', $track);
$url = "https://itunes.apple.com/search?term=$track&media=music&country=US&entity=song&limit=300";

$content = file_get_contents($url);
$json = json_decode($content,true);
foreach ($json['results'] as $item){
    $tempArtistName = ($item[artistName]);
    $newimagelink = ($item[artworkUrl100]);
    if(stripos($tempArtistName,$artist) !== false){
            $newimagelink = ($item[artworkUrl100]);
            echo $newimagelink;
            break;
    }

}

if(strpos($newimagelink,'mzstatic') !== false){
    $newimagelink = str_replace("100x100","225x225",$newimagelink);
    header ("Location: ".$newimagelink);
}else{
    header ("Location: http://server.gursimran.net/default.jpg");
}
?>
