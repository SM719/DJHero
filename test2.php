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

        //$tempArtistName = ($json['results'][0][artistName]);
        //$tempTrackName = ($json['results'][0][trackName]);
        //echo $tempArtistName;

        //$artistName = str_replace(' ', '+', $tempArtistName);
        //$trackName = str_replace(' ', '+', $tempTrackName);
        //$url = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=e378c2c6ca6a39c9ed8ecce01463f1e1&artist=$artistName&track=$trackName&autocorrect=1";
        //$xml = simplexml_load_file($url);
        //$newimagelink = ($json['results'][1][artworkUrl100]);
        //echo $newimagelink;
        if(strpos($newimagelink,'mzstatic') !== false){
                $newimagelink = str_replace("100x100","225x225",$newimagelink);
                header ("Location: ".$newimagelink);
        }else{
                //$newimagelink = str_replace("100x100","600x600",$newimagelink);
                header ("Location: http://server.gursimran.net/default.jpg");
                //echo $newimagelink;
        }
?>
