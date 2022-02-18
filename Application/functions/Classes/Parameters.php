<?php

/**
 * Recove element from json file.
 * 
 * @author JTissier <jtissier78@gmail.com>
 */
class Parameters{
    private $paramLink ;

    public function __construct($link){
        $this->paramLink=$link;
    }
    /**
     * Undocumented function
     * @author JTissier <jtissier78@gmail.com>
     * @param string $categorie
     * @param string $info
     * @return void
     */
    function getParameter(string $categorie, string $info){
        $data=file_get_contents($this->paramLink,true);
        $parameter= json_decode($data,true);
        return $parameter[$categorie][$info];
    }

    function getUserParameter(string $username,string $info)
    {
        $data=file_get_contents($this->paramLink,true);
        $parameter= json_decode($data,true);
        return $parameter["users"][$username][$info];
    }
}