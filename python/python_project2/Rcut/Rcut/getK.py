

import pandas as pd
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt


# df_features = pd.read_csv(r'C:\预处理后数据.csv', encoding='gbk')  # 读入数据
from Rcut import data_processing

'利用SSE选择k'
df_features = data_processing.data_pro('C:/Users/Tommy/Desktop/毕业设计/算法/兰州2015年数据-2.xls', 1)
SSE = []  # 存放每次结果的误差平方和
for k in range(1, 9):
    estimator = KMeans(n_clusters=k)  # 构造聚类器
    estimator.fit(df_features[['R', 'F', 'M']])
    SSE.append(estimator.inertia_)  # estimator.inertia_获取聚类准则的总和
X = range(1, 9)
plt.xlabel('k')
plt.ylabel('SSE')
plt.plot(X, SSE, 'o-')
plt.show()