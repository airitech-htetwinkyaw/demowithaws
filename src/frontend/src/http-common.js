import axios from "axios";

export default axios.create({
  baseURL: "https://t4wkoi27s0.execute-api.us-east-1.amazonaws.com/dev/users",
  // headers: {
  //   "Content-type": "application/json",
  //   "Access-Control-Allow-Origin": "*",
  //   "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS"
  // }
});