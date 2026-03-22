t=David(:,1);
u=David(:,2);
y=David(:,3);

figure
plot(t,u,t,y) %, hold on
dcm = datacursormode(gcf);
set(dcm, 'UpdateFcn', @customCursorUpdateFcn);
%%
iuM = 153;
iyM = 156;
ium = 162;
iym = 166;

dt = t(2)-t(1);
Mr = (y(iyM)-y(iym))/(u(iuM)-u(ium));
Tr = (t(iym)-t(iyM))*2;

tita = sqrt((Mr-sqrt(Mr^2-1))/2/Mr);
wr = (2*pi/Tr);
wn = wr/sqrt(1-2*tita^2);
K = mean(y)/mean(u);

Hs = tf(K*wn^2,[1 2*tita*wn wn^2]);

[num, den] = tfdata(Hs,'v');

A = [0 1;-wn^2 -2*tita*wn];
B = [0;K*wn^2];
C = [1 0];
D = 0;

sys = ss(A,B,C,D);
ysim1 = lsim(sys,u,t,[y(1),(y(2)-y(1))/(t(2)-t(1))])

empn = norm(y-ysim1)/norm(y-mean(y))*100

plot(t,ysim1,t,y)
hold on

% figure
% bode(num,den)
% figure
% nyquist(num,den)
%%
dt = t(2)-t(1)
d_id = iddata(y,u,dt)
%%
m_n4sid=n4sid(d_id,1:15)
figure, compare(d_id,m_n4sid)
figure, resid(d_id,m_n4sid)
%%
M_arx=arx(d_id,[2,2,0])
Hz=tf(M_arx.B,M_arx.A,dt)
% figure, resid(d_id,M_arx,5)
% figure, compare(d_id,M_arx)

Hdz_arx = tf(M_arx)
%Hc_arx = d2c(Hd_arx, 'zoh')
%%
M_armax = armax(d_id,[2,1,2,1])
%figure, resid(d_id,M_armax,5)
%figure, compare(d_id,M_armax)
Hz_armax=tf(M_armax.B,M_armax.A,dt,'variable','z^-1')

Hd_armax = tf(M_armax)
%Hc_armax = d2c(Hd_armax, 'zoh')
%%
M_oe = oe(d_id,[2,2,1])
figure, resid(d_id,M_oe,5)
figure, compare(d_id,M_oe)

Hz_oe=tf(M_oe.B,M_oe.F,dt)

% Hdz_oe = tf(M_oe)
 Hc_oe = d2c(Hz_oe, 'zoh')
%%
M_iv4 = iv4(d_id,[2,2,1])
figure, resid(d_id,M_iv4,5)
figure, compare(d_id,M_iv4)

Hdz_iv4 = tf(M_iv4)
Hc_iv4 = d2c(Hd_iv4, 'zoh')