This a a dummy function for the lenneflow application. 
The function processes the payload, sleeps a given time, creates an output payload and send it to the callback url via post request.

Input payload example
```json
{
    "callBackUrl" : "http://localhost:8100/api/qms/callback/1/1/1",
    "inputData": {
        "sleepTimeInMillis": 5
        }
}
```



Output payload example
```json
{
    "runStatus" : "COMPLETED",
    "failureReason": "",
    "outputData": {
        "sleepTimeInMillis": 5000,
        "sleepTimeInSeconds" : 5
        }
}
