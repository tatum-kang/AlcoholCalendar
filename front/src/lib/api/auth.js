import axios from 'axios';

export const checkEmailExists = (email) => axios.get('/api/auth/exists/email/' + email);
export const checkNicknameExists = (username) => axios.get('/api/auth/exists/username/' + username);

export const localRegister = ({email, name, password, nickname}) => axios.post('/v1/user/signup', { email, name, password, nickname });
export const localLogin = ({email, password}) => axios.post('/v1/user/login', { email, password });

export const checkStatus = () => axios.get('/api/auth/check');
export const logout = () => axios.post('/api/auth/logout');