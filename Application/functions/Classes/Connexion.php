<?php
require_once "Parameters.php";

/**
 * Class Connexion to DB
 * 
 * @author JTissier <jtissier78@gmail.com>
 */
class Connexion{

    private $PARAM_LINK ;
    private $DB_NAME;
    private $DB_HOST;
    private $PARAMS;

    public function __construct($link){
        $this->PARAM_LINK=$link;
        $this->PARAMS=new Parameters($this->PARAM_LINK);
        $this->DB_NAME=$this->PARAMS->getParameter('root','dbname');
        $this->DB_HOST=$this->PARAMS->getParameter('root','host');
        var_dump ($this);
    }
    /**
     * Function to Create DataBase and MasterUser
     * @author JTissier <jtissier78@gmail.com>
     * @return void
     */
    public function DBCreate($DB_ROOTUSER, $DB_ROOTPASSWORD){
        $DB_USER=$params->getParameter('grant','id');
        $DB_PASSWORD=$params->getParameter('grant','psw');
        try {
            $pdo=new PDO("mysql:host={$this->DB_HOST}", $DB_ROOTUSER, $DB_ROOTPASSWORD); 
            $pdo ->exec("CREATE DATABASE IF NOT EXISTS `{$this->DB_NAME}` DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;
            CREATE USER IF NOT EXISTS `$DB_USER`@`{$this->DB_HOST}` IDENTIFIED BY '$DB_PASSWORD';
            GRANT ALL PRIVILEGES ON `{$this->DB_NAME}`.* TO `$DB_USER`@`{$this->DB_HOST}` ;
            FLUSH PRIVILEGES;
            SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));");
        } catch (PDOException $exception) {
            die("Connexion Error ${exception}");
        }
    }

    public function UserCreate(string $userType)
    {
        $DB_GRANT_USER=$params->getParameter('grant','id');
        $DB_GRANT_PASSWORD=$params->getParameter('grant','psw');
        $DB_NEW_USER=$params->getParameter($userType,'id');
        $DB_NEW_PASSWORD=$params->getParameter($userType,'psw');
        try {
            $pdo=new PDO("mysql:host={$this->DB_HOST};dbname={$this->DB_NAME};charset=UTF8",$DB_GRANT_USER,$DB_GRANT_PASSWORD);
            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $pdo->prepare("CREATE USER IF NOT EXISTS `$DB_NEW_USER`@`{$this->DB_HOST}` IDENTIFIED BY '$DB_NEW_PASSWORD';
            GRANT ALL PRIVILEGES ON `{$this->DB_NAME}`.* TO `$DB_NEW_USER`@`{$this->DB_HOST}` ;
            FLUSH PRIVILEGES;");
        } catch (PDOException $except) {
            error_log( "PDO non instance : {$except} ");
        }
    }

    /**
     * Function to Connect PDO
     * 
     * @author JTissier <jtissier78@gmail.com>
     * @return {PDO Object} $pdo Connection to the DB. 
     */
    public function PDOInit(){
        $params=new Parameters($this->paramLink);
        $DB_USER=$params->getParameter('grant','id');
        $DB_PASSWORD=$params->getParameter('grant','psw');
        $DB_NAME=$params->getParameter('root','dbname');
        $DB_HOST=$params->getParameter('root','host');

        try {
            $pdo=new PDO("mysql:host={$DB_HOST};dbname={$DB_NAME}",$DB_USER,$DB_PASSWORD,array(PDO::  MYSQL_ATTR_INIT_COMMAND => 'SET NAMES \'UTF8\''));
            
            return $pdo;
        } catch (PDOException $except) {
            error_log( "PDO non instance : {$except} ");
        }
    }

    /**
     * Execute request.
     *
     * @author JTissier <jtissier78@gmail.com>
     * @param string $request
     * @return void
     */
    public function sendRequest(string $request){
        $connexion= $this->PDOInit();
        $connexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $result= $connexion->prepare($request);
        $result->execute();
        return $result;
    }
}