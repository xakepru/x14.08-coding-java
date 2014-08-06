
function punish(userName) {
    var SendMailUtil = Java.type("ru.xakep.inspector.SendMailUtil");

    var utils = new SendMailUtil();
    utils.sendMail(userName);
}
