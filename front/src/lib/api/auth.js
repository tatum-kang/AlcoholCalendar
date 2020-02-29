import axios from 'axios';

// const headers = { 'Access-Control-Allow-Origin' : '*' }
  
const baseUrl = "http://ec2-15-165-98-23.ap-northeast-2.compute.amazonaws.com:8080/v1/user/"
export const checkEmailExists = (email) => axios.get(baseUrl + 'checkemail?email=' + email);
export const checkNicknameExists = (nickname) => axios.get(baseUrl + 'checknickname?nickname=' + nickname);
export const localRegister = ({email, name, password, nickname}) => {
const body = { email:email, name:name, password:password, nickname:nickname }
axios.post(baseUrl + 'signup', body);
}
export const localLogin = ({email, password}) => axios.post(baseUrl + 'login', { email:email, password:password });
