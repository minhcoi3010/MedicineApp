CREATE DATABASE QuanLyHieuThuocWeb
GO
USE QuanLyHieuThuocWeb

/* --------------- Tạo bảng --------------- */

-- Tạo bảng tNhaCungCap
CREATE TABLE tNhaCungCap
(
	MaNCC		nvarchar(10)	NOT NULL,
	TenNCC		nvarchar(50)	NOT NULL,
	SDT			nvarchar(12),
	Email		nvarchar(20),
	DiaChi		nvarchar(50),
	
	CONSTRAINT PK_tNhaCungCap PRIMARY KEY(MaNCC)
);
GO

-- Tạo bảng tNhanVien
CREATE TABLE tNhanVien
(
	MaNV		nvarchar(10)	NOT NULL,
	TenNV		nvarchar(50)	NOT NULL,
	GioiTinh	nvarchar(10),
	NgaySinh	date,
	DiaChi		nvarchar(50),
	SDT			nvarchar(12),

	CONSTRAINT PK_tNhanVien PRIMARY KEY(MaNV)
);
GO

-- Tạo bảng tKhachHang
CREATE TABLE tKhachHang
(
	MaKH		nvarchar(10)	NOT NULL,
	TenKH		nvarchar(50)	NOT NULL,
	GioiTinh	nvarchar(10),
	NgaySinh	date,
	DiaChi		nvarchar(50),
	SDT			nvarchar(12),

	CONSTRAINT PK_tKhachHang PRIMARY KEY(MaKH)
);
GO

-- Tạo bảng tLogin
CREATE TABLE Login
(
	Account		nvarchar(255)	NOT NULL,
	Password	nvarchar(255)	NOT NULL,
	Role		nvarchar(255)	NULL,

	CONSTRAINT PK_tLogin PRIMARY KEY(Account),
);
GO

-- Tạo bảng tLoaiThuoc
CREATE TABLE tLoaiThuoc
(
	MaLoai		nvarchar(10)	NOT NULL,
	TenLoai		nvarchar(50)	NOT NULL,

	CONSTRAINT PK_tLoaiThuoc PRIMARY KEY(MaLoai)
);
GO

-- Tạo bảng tThuoc
CREATE TABLE tThuoc
(
	MaThuoc		nvarchar(10)	NOT NULL,
	TenThuoc	nvarchar(50)	NOT NULL,
	ThanhPhan	nvarchar(50),
	NgaySX		date,
	NgayHH		date,
	MaLoai		nvarchar(10)	NOT NULL,
	DonGiaBan	float,
	DonGiaNhap	float,
	SoLuong		int,
	TrongLuong	float,
	Anh			nvarchar(15),

	CONSTRAINT PK_tThuoc PRIMARY KEY(MaThuoc),
	CONSTRAINT FK_tThuoc_MaLoai FOREIGN KEY(MaLoai) REFERENCES tLoaiThuoc(MaLoai),
	CONSTRAINT CHK_NGAYSX_NGAYHH CHECK(NGAYSX < NGAYHH)
);
GO

-- Tạo bảng tHoaDonNhap
CREATE TABLE tHoaDonNhap
(
	SoHDN		nvarchar(10)	NOT NULL,
	MaNV		nvarchar(10)	NOT NULL,
	NgayLap		date,
	MaNCC		nvarchar(10)	NOT NULL,
	TongTien	float,

	CONSTRAINT PK_tHoaDonNhap PRIMARY KEY(SoHDN),
	CONSTRAINT FK_tHoaDonNhap_MaNV FOREIGN KEY(MaNV) REFERENCES tNhanVien(MaNV),
	CONSTRAINT FK_tHoaDonNhap_MaNCC FOREIGN KEY(MaNCC) REFERENCES tNhaCungCap(MaNCC)
);
GO

-- Tạo bảng tChiTietHDN
CREATE TABLE tChiTietHDN
(
	SoHDN		nvarchar(10)	NOT NULL,
	MaThuoc		nvarchar(10)	NOT NULL,
	SLNhap		int,
	KhuyenMai	nvarchar(10),
	ThanhTien	float,

	CONSTRAINT PK_tChiTietHDN PRIMARY KEY(SoHDN, MaThuoc),
	CONSTRAINT FK_tChiTietHDN_SoHDN FOREIGN KEY(SoHDN) REFERENCES tHoaDonNhap(SoHDN),
	CONSTRAINT FK_tChiTietHDN_MaThuoc FOREIGN KEY(MaThuoc) REFERENCES tThuoc(MaThuoc)
);
GO

-- Tạo bảng tHoaDonBan
CREATE TABLE tHoaDonBan
(
	SoHDB		nvarchar(10)	NOT NULL,
	MaNV		nvarchar(10)	NOT NULL,
	NgayLap		date,
	MaKH		nvarchar(10)	NOT NULL,
	TongTien	float,

	CONSTRAINT PK_tHoaDonBan PRIMARY KEY(SoHDB),
	CONSTRAINT FK_tHoaDonBan_MaNV FOREIGN KEY(MaNV) REFERENCES tNhanVien(MaNV),
	CONSTRAINT FK_tHoaDonBan_MaKH FOREIGN KEY(MaKH) REFERENCES tKhachHang(MaKH)
);
GO

-- Tạo bảng tChiTietHDB
CREATE TABLE tChiTietHDB
(
	SoHDB		nvarchar(10)	NOT NULL,
	MaThuoc		nvarchar(10)	NOT NULL,
	SLBan		int,
	KhuyenMai	nvarchar(10),
	ThanhTien	float,

	CONSTRAINT PK_tChiTietHDB PRIMARY KEY(SoHDB, MaThuoc),
	CONSTRAINT FK_tChiTietHDB_SoHDB FOREIGN KEY(SoHDB) REFERENCES tHoaDonBan(SoHDB),
	CONSTRAINT FK_tChiTietHDB_MaThuoc FOREIGN KEY(MaThuoc) REFERENCES tThuoc(MaThuoc)
);
GO

/* --------------- Thêm dữ liệu vào bảng --------------- */
-- tNhaCungCap
INSERT INTO [dbo].[tNhaCungCap] ([MaNCC], [TenNCC], [SDT], [Email], [DiaChi]) VALUES (N'NCC01', N'TUẤN1', N'0928392839', N'TUAN@GMAIL.COM', N'HƯNG YÊN')
INSERT INTO [dbo].[tNhaCungCap] ([MaNCC], [TenNCC], [SDT], [Email], [DiaChi]) VALUES (N'NCC02', N'HẢI', N'0394893843', N'HAI@GMAIL.COM', N'THÁI BÌNH')
INSERT INTO [dbo].[tNhaCungCap] ([MaNCC], [TenNCC], [SDT], [Email], [DiaChi]) VALUES (N'NCC03', N'Sang', N'0394893843', N'SANG@GMAIL.COM', N'PHÚ THỌ')
INSERT INTO [dbo].[tNhaCungCap] ([MaNCC], [TenNCC], [SDT], [Email], [DiaChi]) VALUES (N'NCC04', N'HẢI ANH', N'0394938934', N'HAIANH@GMAIL.COM', N'HƯNG YÊN')

-- tNhanVien
INSERT INTO [dbo].[tNhanVien] ([MaNV], [TenNV], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'NV01', N'MINH COI', N'Nam', N'2003-10-30', N'THÁI BÌNH', N'0379175759')
INSERT INTO [dbo].[tNhanVien] ([MaNV], [TenNV], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'NV02', N'TIẾN', N'Nam', N'2005-01-01', N'HƯNG YÊN', N'0394893843')
INSERT INTO [dbo].[tNhanVien] ([MaNV], [TenNV], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'NV03', N'HẢI', N'Nữ', N'2003-01-01', N'HƯNG YÊN', N'0394938934')
INSERT INTO [dbo].[tNhanVien] ([MaNV], [TenNV], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'NV04', N'MINH LÙN', N'Nam', N'2003-01-01', N'HƯNG YÊN', N'0394893843')
INSERT INTO [dbo].[tNhanVien] ([MaNV], [TenNV], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'NV05', N'TIẾN', N'NAM', N'2023-10-06', N'HƯNG YÊN', N'0394893843')

-- tKhachHang
INSERT INTO [dbo].[tKhachHang] ([MaKH], [TenKH], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'KH01', N'HẢI3', N'Nam', N'2003-01-01', N'PHÚ THỌ', N'0394893845')
INSERT INTO [dbo].[tKhachHang] ([MaKH], [TenKH], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'KH02', N'HẢI3', N'Nam', N'2003-01-01', N'HƯNG YÊN', N'0394893843')
INSERT INTO [dbo].[tKhachHang] ([MaKH], [TenKH], [GioiTinh], [NgaySinh], [DiaChi], [SDT]) VALUES (N'KH03', N'HẢI3', N'Nam', N'2023-10-14', N'HƯNG YÊN', N'0394893843')

--Login
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'a', N'abc', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'ab', N'abc', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'abc', N'abc123', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'abc111', N'abc123', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'abc11122', N'abc123', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Admin1', N'Admin1', N'Admin')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Hoanghiep', N'hiephoang', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Kien', N'kien', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Kien111', N'kien111', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'kienancuc', N'kienancuc', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Minh', N'Minhcoi123', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Minh1', N'Minhcoi12', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Nguyenminh', N'minh123456', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Tuananh', N'tuananh123', N'User')
INSERT INTO [dbo].[Login] ([Account], [Password], [Role]) VALUES (N'Tuananh1', N'abc123', N'User')

-- tLoaiThuoc
INSERT INTO [dbo].[tLoaiThuoc] ([MaLoai], [TenLoai]) VALUES (N'ML01', N'THUỐC HO')
INSERT INTO [dbo].[tLoaiThuoc] ([MaLoai], [TenLoai]) VALUES (N'ML02', N'THUỐC BỔ')
INSERT INTO [dbo].[tLoaiThuoc] ([MaLoai], [TenLoai]) VALUES (N'ML03', N'THUỐC GAN')
INSERT INTO [dbo].[tLoaiThuoc] ([MaLoai], [TenLoai]) VALUES (N'ML04', N'THUỐC TIM')
INSERT INTO [dbo].[tLoaiThuoc] ([MaLoai], [TenLoai]) VALUES (N'ML05', N'THUỐC KHỚP')


-- tThuoc
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT01', N'Ama-Power', N'Ampicillin', N'2023-01-01', N'2023-12-01', N'ML01', 100000, 90000, 10, 1000, N'products-1.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT02', N'Bacsulfo', N'Cefoperazon', N'2023-01-01', N'2023-12-01', N'ML01', 100000, 90000, 100, 300, N'products-2.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT03', N'Bi-Daphazyl', N'Metronidazole', N'2023-01-01', N'2023-12-01', N'ML01', 100000, 90000, 100, 300, N'products-3.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT04', N'Cecopha', N'Cefuroxim', N'2023-01-01', N'2023-12-01', N'ML02', 100000, 80000, 100, 300, N'products-4.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT05', N'Ceftitoz', N'Ceftibuten', N'2023-01-01', N'2023-12-01', N'ML02', 100000, 80000, 100, 300, N'products-5.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT06', N'Cloromycetin', N'Cloramphenicol', N'2023-01-01', N'2023-12-01', N'ML03', 100000, 70000, 100, 300, N'products-6.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT07', N'Fosformed', N'Fosfomycin', N'2023-01-01', N'2023-12-01', N'ML03', 100000, 75000, 100, 300, N'products-7.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT08', N'Francefdi', N'Cefdinir', N'2023-10-01', N'2023-10-15', N'ML01', 100000, 85000, 100, 300, N'products-8.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT09', N'Opxixa', N'Opxixa', N'2023-11-01', N'2023-11-30', N'ML04', 100000, 10000, 50, 300, N'products-10.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT10', N'Imexime', N'Cefixim', N'2023-10-01', N'2023-10-21', N'ML01', 100000, 95000, 100, 300, N'products-9.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT11', N'Phabaren', N'Phabaren', N'2023-11-01', N'2023-11-30', N'ML05', 100000, 10000, 50, 300, N'products-11.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT12', N'Pharmox', N'Pharmox', N'2023-11-01', N'2023-11-30', N'ML04', 100000, 10000, 100, 300, N'products-12.jpg')
INSERT INTO [dbo].[tThuoc] ([MaThuoc], [TenThuoc], [ThanhPhan], [NgaySX], [NgayHH], [MaLoai], [DonGiaBan], [DonGiaNhap], [SoLuong], [TrongLuong], [Anh]) VALUES (N'MT13', N'Pharmox', N'Pharmox', N'2023-11-01', N'2023-11-30', N'ML05', 100000, 10000, 100, 300, N'products-13.jpg')


-- tHoaDonNhap
INSERT INTO [dbo].[tHoaDonNhap] ([SoHDN], [MaNV], [NgayLap], [MaNCC], [TongTien]) VALUES (N'HDN01', N'NV01', N'2022-01-02', N'NCC03', 1000000)
INSERT INTO [dbo].[tHoaDonNhap] ([SoHDN], [MaNV], [NgayLap], [MaNCC], [TongTien]) VALUES (N'HDN02', N'NV02', N'2023-01-01', N'NCC01', 1000000)
INSERT INTO [dbo].[tHoaDonNhap] ([SoHDN], [MaNV], [NgayLap], [MaNCC], [TongTien]) VALUES (N'HDN03', N'NV05', N'2023-11-03', N'NCC04', 100000)
INSERT INTO [dbo].[tHoaDonNhap] ([SoHDN], [MaNV], [NgayLap], [MaNCC], [TongTien]) VALUES (N'HDN04', N'NV04', N'2023-11-03', N'NCC02', 1000000)


-- tChiTietHDN
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN01', N'MT01', 100, N'10', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN01', N'MT04', 10, N'100', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN01', N'MT11', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN02', N'MT01', 10, N'100', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN02', N'MT04', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN02', N'MT07', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN03', N'MT03', 10, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN03', N'MT06', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN03', N'MT11', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN04', N'MT03', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN04', N'MT04', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDN] ([SoHDN], [MaThuoc], [SLNhap], [KhuyenMai], [ThanhTien]) VALUES (N'HDN04', N'MT08', 9, N'9', 1000000)

-- tHoaDonBan
INSERT INTO [dbo].[tHoaDonBan] ([SoHDB], [MaNV], [NgayLap], [MaKH], [TongTien]) VALUES (N'HDB01', N'NV01', N'2022-03-04', N'KH01', 10000000)
INSERT INTO [dbo].[tHoaDonBan] ([SoHDB], [MaNV], [NgayLap], [MaKH], [TongTien]) VALUES (N'HDB02', N'NV02', N'2023-02-04', N'KH02', 10000000)
INSERT INTO [dbo].[tHoaDonBan] ([SoHDB], [MaNV], [NgayLap], [MaKH], [TongTien]) VALUES (N'HDB03', N'NV02', N'2005-01-01', N'KH02', 1000001)
INSERT INTO [dbo].[tHoaDonBan] ([SoHDB], [MaNV], [NgayLap], [MaKH], [TongTien]) VALUES (N'HDB04', N'NV04', N'2023-11-01', N'KH03', 1000000)

-- tChiTietHDB
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB01', N'MT01', 9, N'10', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB01', N'MT04', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB01', N'MT08', 9, N'10', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB02', N'MT01', 9, N'9', 1000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB02', N'MT04', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB02', N'MT09', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB03', N'MT01', 9, N'100', 1000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB03', N'MT04', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB03', N'MT10', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB04', N'MT01', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB04', N'MT07', 9, N'9', 2000000)
INSERT INTO [dbo].[tChiTietHDB] ([SoHDB], [MaThuoc], [SLBan], [KhuyenMai], [ThanhTien]) VALUES (N'HDB04', N'MT09', 9, N'9', 2000000)


