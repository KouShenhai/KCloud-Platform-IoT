"use client"
import React from 'react'
import * as icons from '@ant-design/icons'
const iconsArr = [
	{
		"icon":<icons.AccountBookFilled style={{ fontSize: '34px' }} />,
		"title":"AccountBookFilled"
	},
	{
		"icon":<icons.AccountBookOutlined style={{ fontSize: '34px' }} />,
		"title":"AccountBookOutlined"
	},
	{
		"icon":<icons.AccountBookTwoTone style={{ fontSize: '34px' }} />,
		"title":"AccountBookTwoTone"
	},
	{
		"icon":<icons.AimOutlined style={{ fontSize: '34px' }} />,
		"title":"AimOutlined"
	},
	{
		"icon":<icons.AlertFilled style={{ fontSize: '34px' }} />,
		"title":"AlertFilled"
	},
	{
		"icon":<icons.AlertOutlined style={{ fontSize: '34px' }} />,
		"title":"AlertOutlined"
	},
	{
		"icon":<icons.AlertTwoTone style={{ fontSize: '34px' }} />,
		"title":"AlertTwoTone"
	},
	{
		"icon":<icons.AlibabaOutlined style={{ fontSize: '34px' }} />,
		"title":"AlibabaOutlined"
	},
	{
		"icon":<icons.AlignCenterOutlined style={{ fontSize: '34px' }} />,
		"title":"AlignCenterOutlined"
	},
	{
		"icon":<icons.AlignLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"AlignLeftOutlined"
	},
	{
		"icon":<icons.AlignRightOutlined style={{ fontSize: '34px' }} />,
		"title":"AlignRightOutlined"
	},
	{
		"icon":<icons.AlipayCircleFilled style={{ fontSize: '34px' }} />,
		"title":"AlipayCircleFilled"
	},
	{
		"icon":<icons.AlipayCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"AlipayCircleOutlined"
	},
	{
		"icon":<icons.AlipayOutlined style={{ fontSize: '34px' }} />,
		"title":"AlipayOutlined"
	},
	{
		"icon":<icons.AlipaySquareFilled style={{ fontSize: '34px' }} />,
		"title":"AlipaySquareFilled"
	},
	{
		"icon":<icons.AliwangwangFilled style={{ fontSize: '34px' }} />,
		"title":"AliwangwangFilled"
	},
	{
		"icon":<icons.AliwangwangOutlined style={{ fontSize: '34px' }} />,
		"title":"AliwangwangOutlined"
	},
	{
		"icon":<icons.AliyunOutlined style={{ fontSize: '34px' }} />,
		"title":"AliyunOutlined"
	},
	{
		"icon":<icons.AmazonCircleFilled style={{ fontSize: '34px' }} />,
		"title":"AmazonCircleFilled"
	},
	{
		"icon":<icons.AmazonOutlined style={{ fontSize: '34px' }} />,
		"title":"AmazonOutlined"
	},
	{
		"icon":<icons.AmazonSquareFilled style={{ fontSize: '34px' }} />,
		"title":"AmazonSquareFilled"
	},
	{
		"icon":<icons.AndroidFilled style={{ fontSize: '34px' }} />,
		"title":"AndroidFilled"
	},
	{
		"icon":<icons.AndroidOutlined style={{ fontSize: '34px' }} />,
		"title":"AndroidOutlined"
	},
	{
		"icon":<icons.AntCloudOutlined style={{ fontSize: '34px' }} />,
		"title":"AntCloudOutlined"
	},
	{
		"icon":<icons.AntDesignOutlined style={{ fontSize: '34px' }} />,
		"title":"AntDesignOutlined"
	},
	{
		"icon":<icons.ApartmentOutlined style={{ fontSize: '34px' }} />,
		"title":"ApartmentOutlined"
	},
	{
		"icon":<icons.ApiFilled style={{ fontSize: '34px' }} />,
		"title":"ApiFilled"
	},
	{
		"icon":<icons.ApiOutlined style={{ fontSize: '34px' }} />,
		"title":"ApiOutlined"
	},
	{
		"icon":<icons.ApiTwoTone style={{ fontSize: '34px' }} />,
		"title":"ApiTwoTone"
	},
	{
		"icon":<icons.AppleFilled style={{ fontSize: '34px' }} />,
		"title":"AppleFilled"
	},
	{
		"icon":<icons.AppleOutlined style={{ fontSize: '34px' }} />,
		"title":"AppleOutlined"
	},
	{
		"icon":<icons.AppstoreAddOutlined style={{ fontSize: '34px' }} />,
		"title":"AppstoreAddOutlined"
	},
	{
		"icon":<icons.AppstoreFilled style={{ fontSize: '34px' }} />,
		"title":"AppstoreFilled"
	},
	{
		"icon":<icons.AppstoreOutlined style={{ fontSize: '34px' }} />,
		"title":"AppstoreOutlined"
	},
	{
		"icon":<icons.AppstoreTwoTone style={{ fontSize: '34px' }} />,
		"title":"AppstoreTwoTone"
	},
	{
		"icon":<icons.AreaChartOutlined style={{ fontSize: '34px' }} />,
		"title":"AreaChartOutlined"
	},
	{
		"icon":<icons.ArrowDownOutlined style={{ fontSize: '34px' }} />,
		"title":"ArrowDownOutlined"
	},
	{
		"icon":<icons.ArrowLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"ArrowLeftOutlined"
	},
	{
		"icon":<icons.ArrowRightOutlined style={{ fontSize: '34px' }} />,
		"title":"ArrowRightOutlined"
	},
	{
		"icon":<icons.ArrowUpOutlined style={{ fontSize: '34px' }} />,
		"title":"ArrowUpOutlined"
	},
	{
		"icon":<icons.ArrowsAltOutlined style={{ fontSize: '34px' }} />,
		"title":"ArrowsAltOutlined"
	},
	{
		"icon":<icons.AudioFilled style={{ fontSize: '34px' }} />,
		"title":"AudioFilled"
	},
	{
		"icon":<icons.AudioMutedOutlined style={{ fontSize: '34px' }} />,
		"title":"AudioMutedOutlined"
	},
	{
		"icon":<icons.AudioOutlined style={{ fontSize: '34px' }} />,
		"title":"AudioOutlined"
	},
	{
		"icon":<icons.AudioTwoTone style={{ fontSize: '34px' }} />,
		"title":"AudioTwoTone"
	},
	{
		"icon":<icons.AuditOutlined style={{ fontSize: '34px' }} />,
		"title":"AuditOutlined"
	},
	{
		"icon":<icons.BackwardFilled style={{ fontSize: '34px' }} />,
		"title":"BackwardFilled"
	},
	{
		"icon":<icons.BackwardOutlined style={{ fontSize: '34px' }} />,
		"title":"BackwardOutlined"
	},
	{
		"icon":<icons.BaiduOutlined style={{ fontSize: '34px' }} />,
		"title":"BaiduOutlined"
	},
	{
		"icon":<icons.BankFilled style={{ fontSize: '34px' }} />,
		"title":"BankFilled"
	},
	{
		"icon":<icons.BankOutlined style={{ fontSize: '34px' }} />,
		"title":"BankOutlined"
	},
	{
		"icon":<icons.BankTwoTone style={{ fontSize: '34px' }} />,
		"title":"BankTwoTone"
	},
	{
		"icon":<icons.BarChartOutlined style={{ fontSize: '34px' }} />,
		"title":"BarChartOutlined"
	},
	{
		"icon":<icons.BarcodeOutlined style={{ fontSize: '34px' }} />,
		"title":"BarcodeOutlined"
	},
	{
		"icon":<icons.BarsOutlined style={{ fontSize: '34px' }} />,
		"title":"BarsOutlined"
	},
	{
		"icon":<icons.BehanceCircleFilled style={{ fontSize: '34px' }} />,
		"title":"BehanceCircleFilled"
	},
	{
		"icon":<icons.BehanceOutlined style={{ fontSize: '34px' }} />,
		"title":"BehanceOutlined"
	},
	{
		"icon":<icons.BehanceSquareFilled style={{ fontSize: '34px' }} />,
		"title":"BehanceSquareFilled"
	},
	{
		"icon":<icons.BehanceSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"BehanceSquareOutlined"
	},
	{
		"icon":<icons.BellFilled style={{ fontSize: '34px' }} />,
		"title":"BellFilled"
	},
	{
		"icon":<icons.BellOutlined style={{ fontSize: '34px' }} />,
		"title":"BellOutlined"
	},
	{
		"icon":<icons.BellTwoTone style={{ fontSize: '34px' }} />,
		"title":"BellTwoTone"
	},
	{
		"icon":<icons.BgColorsOutlined style={{ fontSize: '34px' }} />,
		"title":"BgColorsOutlined"
	},
	{
		"icon":<icons.BilibiliFilled style={{ fontSize: '34px' }} />,
		"title":"BilibiliFilled"
	},
	{
		"icon":<icons.BilibiliOutlined style={{ fontSize: '34px' }} />,
		"title":"BilibiliOutlined"
	},
	{
		"icon":<icons.BlockOutlined style={{ fontSize: '34px' }} />,
		"title":"BlockOutlined"
	},
	{
		"icon":<icons.BoldOutlined style={{ fontSize: '34px' }} />,
		"title":"BoldOutlined"
	},
	{
		"icon":<icons.BookFilled style={{ fontSize: '34px' }} />,
		"title":"BookFilled"
	},
	{
		"icon":<icons.BookOutlined style={{ fontSize: '34px' }} />,
		"title":"BookOutlined"
	},
	{
		"icon":<icons.BookTwoTone style={{ fontSize: '34px' }} />,
		"title":"BookTwoTone"
	},
	{
		"icon":<icons.BorderBottomOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderBottomOutlined"
	},
	{
		"icon":<icons.BorderHorizontalOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderHorizontalOutlined"
	},
	{
		"icon":<icons.BorderInnerOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderInnerOutlined"
	},
	{
		"icon":<icons.BorderLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderLeftOutlined"
	},
	{
		"icon":<icons.BorderOuterOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderOuterOutlined"
	},
	{
		"icon":<icons.BorderOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderOutlined"
	},
	{
		"icon":<icons.BorderRightOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderRightOutlined"
	},
	{
		"icon":<icons.BorderTopOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderTopOutlined"
	},
	{
		"icon":<icons.BorderVerticleOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderVerticleOutlined"
	},
	{
		"icon":<icons.BorderlessTableOutlined style={{ fontSize: '34px' }} />,
		"title":"BorderlessTableOutlined"
	},
	{
		"icon":<icons.BoxPlotFilled style={{ fontSize: '34px' }} />,
		"title":"BoxPlotFilled"
	},
	{
		"icon":<icons.BoxPlotOutlined style={{ fontSize: '34px' }} />,
		"title":"BoxPlotOutlined"
	},
	{
		"icon":<icons.BoxPlotTwoTone style={{ fontSize: '34px' }} />,
		"title":"BoxPlotTwoTone"
	},
	{
		"icon":<icons.BranchesOutlined style={{ fontSize: '34px' }} />,
		"title":"BranchesOutlined"
	},
	{
		"icon":<icons.BugFilled style={{ fontSize: '34px' }} />,
		"title":"BugFilled"
	},
	{
		"icon":<icons.BugOutlined style={{ fontSize: '34px' }} />,
		"title":"BugOutlined"
	},
	{
		"icon":<icons.BugTwoTone style={{ fontSize: '34px' }} />,
		"title":"BugTwoTone"
	},
	{
		"icon":<icons.BuildFilled style={{ fontSize: '34px' }} />,
		"title":"BuildFilled"
	},
	{
		"icon":<icons.BuildOutlined style={{ fontSize: '34px' }} />,
		"title":"BuildOutlined"
	},
	{
		"icon":<icons.BuildTwoTone style={{ fontSize: '34px' }} />,
		"title":"BuildTwoTone"
	},
	{
		"icon":<icons.BulbFilled style={{ fontSize: '34px' }} />,
		"title":"BulbFilled"
	},
	{
		"icon":<icons.BulbOutlined style={{ fontSize: '34px' }} />,
		"title":"BulbOutlined"
	},
	{
		"icon":<icons.BulbTwoTone style={{ fontSize: '34px' }} />,
		"title":"BulbTwoTone"
	},
	{
		"icon":<icons.CalculatorFilled style={{ fontSize: '34px' }} />,
		"title":"CalculatorFilled"
	},
	{
		"icon":<icons.CalculatorOutlined style={{ fontSize: '34px' }} />,
		"title":"CalculatorOutlined"
	},
	{
		"icon":<icons.CalculatorTwoTone style={{ fontSize: '34px' }} />,
		"title":"CalculatorTwoTone"
	},
	{
		"icon":<icons.CalendarFilled style={{ fontSize: '34px' }} />,
		"title":"CalendarFilled"
	},
	{
		"icon":<icons.CalendarOutlined style={{ fontSize: '34px' }} />,
		"title":"CalendarOutlined"
	},
	{
		"icon":<icons.CalendarTwoTone style={{ fontSize: '34px' }} />,
		"title":"CalendarTwoTone"
	},
	{
		"icon":<icons.CameraFilled style={{ fontSize: '34px' }} />,
		"title":"CameraFilled"
	},
	{
		"icon":<icons.CameraOutlined style={{ fontSize: '34px' }} />,
		"title":"CameraOutlined"
	},
	{
		"icon":<icons.CameraTwoTone style={{ fontSize: '34px' }} />,
		"title":"CameraTwoTone"
	},
	{
		"icon":<icons.CarFilled style={{ fontSize: '34px' }} />,
		"title":"CarFilled"
	},
	{
		"icon":<icons.CarOutlined style={{ fontSize: '34px' }} />,
		"title":"CarOutlined"
	},
	{
		"icon":<icons.CarTwoTone style={{ fontSize: '34px' }} />,
		"title":"CarTwoTone"
	},
	{
		"icon":<icons.CaretDownFilled style={{ fontSize: '34px' }} />,
		"title":"CaretDownFilled"
	},
	{
		"icon":<icons.CaretDownOutlined style={{ fontSize: '34px' }} />,
		"title":"CaretDownOutlined"
	},
	{
		"icon":<icons.CaretLeftFilled style={{ fontSize: '34px' }} />,
		"title":"CaretLeftFilled"
	},
	{
		"icon":<icons.CaretLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"CaretLeftOutlined"
	},
	{
		"icon":<icons.CaretRightFilled style={{ fontSize: '34px' }} />,
		"title":"CaretRightFilled"
	},
	{
		"icon":<icons.CaretRightOutlined style={{ fontSize: '34px' }} />,
		"title":"CaretRightOutlined"
	},
	{
		"icon":<icons.CaretUpFilled style={{ fontSize: '34px' }} />,
		"title":"CaretUpFilled"
	},
	{
		"icon":<icons.CaretUpOutlined style={{ fontSize: '34px' }} />,
		"title":"CaretUpOutlined"
	},
	{
		"icon":<icons.CarryOutFilled style={{ fontSize: '34px' }} />,
		"title":"CarryOutFilled"
	},
	{
		"icon":<icons.CarryOutOutlined style={{ fontSize: '34px' }} />,
		"title":"CarryOutOutlined"
	},
	{
		"icon":<icons.CarryOutTwoTone style={{ fontSize: '34px' }} />,
		"title":"CarryOutTwoTone"
	},
	{
		"icon":<icons.CheckCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CheckCircleFilled"
	},
	{
		"icon":<icons.CheckCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"CheckCircleOutlined"
	},
	{
		"icon":<icons.CheckCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"CheckCircleTwoTone"
	},
	{
		"icon":<icons.CheckOutlined style={{ fontSize: '34px' }} />,
		"title":"CheckOutlined"
	},
	{
		"icon":<icons.CheckSquareFilled style={{ fontSize: '34px' }} />,
		"title":"CheckSquareFilled"
	},
	{
		"icon":<icons.CheckSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"CheckSquareOutlined"
	},
	{
		"icon":<icons.CheckSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"CheckSquareTwoTone"
	},
	{
		"icon":<icons.ChromeFilled style={{ fontSize: '34px' }} />,
		"title":"ChromeFilled"
	},
	{
		"icon":<icons.ChromeOutlined style={{ fontSize: '34px' }} />,
		"title":"ChromeOutlined"
	},
	{
		"icon":<icons.CiCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CiCircleFilled"
	},
	{
		"icon":<icons.CiCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"CiCircleOutlined"
	},
	{
		"icon":<icons.CiCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"CiCircleTwoTone"
	},
	{
		"icon":<icons.CiOutlined style={{ fontSize: '34px' }} />,
		"title":"CiOutlined"
	},
	{
		"icon":<icons.CiTwoTone style={{ fontSize: '34px' }} />,
		"title":"CiTwoTone"
	},
	{
		"icon":<icons.ClearOutlined style={{ fontSize: '34px' }} />,
		"title":"ClearOutlined"
	},
	{
		"icon":<icons.ClockCircleFilled style={{ fontSize: '34px' }} />,
		"title":"ClockCircleFilled"
	},
	{
		"icon":<icons.ClockCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"ClockCircleOutlined"
	},
	{
		"icon":<icons.ClockCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"ClockCircleTwoTone"
	},
	{
		"icon":<icons.CloseCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CloseCircleFilled"
	},
	{
		"icon":<icons.CloseCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"CloseCircleOutlined"
	},
	{
		"icon":<icons.CloseCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"CloseCircleTwoTone"
	},
	{
		"icon":<icons.CloseOutlined style={{ fontSize: '34px' }} />,
		"title":"CloseOutlined"
	},
	{
		"icon":<icons.CloseSquareFilled style={{ fontSize: '34px' }} />,
		"title":"CloseSquareFilled"
	},
	{
		"icon":<icons.CloseSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"CloseSquareOutlined"
	},
	{
		"icon":<icons.CloseSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"CloseSquareTwoTone"
	},
	{
		"icon":<icons.CloudDownloadOutlined style={{ fontSize: '34px' }} />,
		"title":"CloudDownloadOutlined"
	},
	{
		"icon":<icons.CloudFilled style={{ fontSize: '34px' }} />,
		"title":"CloudFilled"
	},
	{
		"icon":<icons.CloudOutlined style={{ fontSize: '34px' }} />,
		"title":"CloudOutlined"
	},
	{
		"icon":<icons.CloudServerOutlined style={{ fontSize: '34px' }} />,
		"title":"CloudServerOutlined"
	},
	{
		"icon":<icons.CloudSyncOutlined style={{ fontSize: '34px' }} />,
		"title":"CloudSyncOutlined"
	},
	{
		"icon":<icons.CloudTwoTone style={{ fontSize: '34px' }} />,
		"title":"CloudTwoTone"
	},
	{
		"icon":<icons.CloudUploadOutlined style={{ fontSize: '34px' }} />,
		"title":"CloudUploadOutlined"
	},
	{
		"icon":<icons.ClusterOutlined style={{ fontSize: '34px' }} />,
		"title":"ClusterOutlined"
	},
	{
		"icon":<icons.CodeFilled style={{ fontSize: '34px' }} />,
		"title":"CodeFilled"
	},
	{
		"icon":<icons.CodeOutlined style={{ fontSize: '34px' }} />,
		"title":"CodeOutlined"
	},
	{
		"icon":<icons.CodeSandboxCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CodeSandboxCircleFilled"
	},
	{
		"icon":<icons.CodeSandboxOutlined style={{ fontSize: '34px' }} />,
		"title":"CodeSandboxOutlined"
	},
	{
		"icon":<icons.CodeSandboxSquareFilled style={{ fontSize: '34px' }} />,
		"title":"CodeSandboxSquareFilled"
	},
	{
		"icon":<icons.CodeTwoTone style={{ fontSize: '34px' }} />,
		"title":"CodeTwoTone"
	},
	{
		"icon":<icons.CodepenCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CodepenCircleFilled"
	},
	{
		"icon":<icons.CodepenCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"CodepenCircleOutlined"
	},
	{
		"icon":<icons.CodepenOutlined style={{ fontSize: '34px' }} />,
		"title":"CodepenOutlined"
	},
	{
		"icon":<icons.CodepenSquareFilled style={{ fontSize: '34px' }} />,
		"title":"CodepenSquareFilled"
	},
	{
		"icon":<icons.CoffeeOutlined style={{ fontSize: '34px' }} />,
		"title":"CoffeeOutlined"
	},
	{
		"icon":<icons.ColumnHeightOutlined style={{ fontSize: '34px' }} />,
		"title":"ColumnHeightOutlined"
	},
	{
		"icon":<icons.ColumnWidthOutlined style={{ fontSize: '34px' }} />,
		"title":"ColumnWidthOutlined"
	},
	{
		"icon":<icons.CommentOutlined style={{ fontSize: '34px' }} />,
		"title":"CommentOutlined"
	},
	{
		"icon":<icons.CompassFilled style={{ fontSize: '34px' }} />,
		"title":"CompassFilled"
	},
	{
		"icon":<icons.CompassOutlined style={{ fontSize: '34px' }} />,
		"title":"CompassOutlined"
	},
	{
		"icon":<icons.CompassTwoTone style={{ fontSize: '34px' }} />,
		"title":"CompassTwoTone"
	},
	{
		"icon":<icons.CompressOutlined style={{ fontSize: '34px' }} />,
		"title":"CompressOutlined"
	},
	{
		"icon":<icons.ConsoleSqlOutlined style={{ fontSize: '34px' }} />,
		"title":"ConsoleSqlOutlined"
	},
	{
		"icon":<icons.ContactsFilled style={{ fontSize: '34px' }} />,
		"title":"ContactsFilled"
	},
	{
		"icon":<icons.ContactsOutlined style={{ fontSize: '34px' }} />,
		"title":"ContactsOutlined"
	},
	{
		"icon":<icons.ContactsTwoTone style={{ fontSize: '34px' }} />,
		"title":"ContactsTwoTone"
	},
	{
		"icon":<icons.ContainerFilled style={{ fontSize: '34px' }} />,
		"title":"ContainerFilled"
	},
	{
		"icon":<icons.ContainerOutlined style={{ fontSize: '34px' }} />,
		"title":"ContainerOutlined"
	},
	{
		"icon":<icons.ContainerTwoTone style={{ fontSize: '34px' }} />,
		"title":"ContainerTwoTone"
	},
	{
		"icon":<icons.ControlFilled style={{ fontSize: '34px' }} />,
		"title":"ControlFilled"
	},
	{
		"icon":<icons.ControlOutlined style={{ fontSize: '34px' }} />,
		"title":"ControlOutlined"
	},
	{
		"icon":<icons.ControlTwoTone style={{ fontSize: '34px' }} />,
		"title":"ControlTwoTone"
	},
	{
		"icon":<icons.CopyFilled style={{ fontSize: '34px' }} />,
		"title":"CopyFilled"
	},
	{
		"icon":<icons.CopyOutlined style={{ fontSize: '34px' }} />,
		"title":"CopyOutlined"
	},
	{
		"icon":<icons.CopyTwoTone style={{ fontSize: '34px' }} />,
		"title":"CopyTwoTone"
	},
	{
		"icon":<icons.CopyrightCircleFilled style={{ fontSize: '34px' }} />,
		"title":"CopyrightCircleFilled"
	},
	{
		"icon":<icons.CopyrightCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"CopyrightCircleOutlined"
	},
	{
		"icon":<icons.CopyrightCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"CopyrightCircleTwoTone"
	},
	{
		"icon":<icons.CopyrightOutlined style={{ fontSize: '34px' }} />,
		"title":"CopyrightOutlined"
	},
	{
		"icon":<icons.CopyrightTwoTone style={{ fontSize: '34px' }} />,
		"title":"CopyrightTwoTone"
	},
	{
		"icon":<icons.CreditCardFilled style={{ fontSize: '34px' }} />,
		"title":"CreditCardFilled"
	},
	{
		"icon":<icons.CreditCardOutlined style={{ fontSize: '34px' }} />,
		"title":"CreditCardOutlined"
	},
	{
		"icon":<icons.CreditCardTwoTone style={{ fontSize: '34px' }} />,
		"title":"CreditCardTwoTone"
	},
	{
		"icon":<icons.CrownFilled style={{ fontSize: '34px' }} />,
		"title":"CrownFilled"
	},
	{
		"icon":<icons.CrownOutlined style={{ fontSize: '34px' }} />,
		"title":"CrownOutlined"
	},
	{
		"icon":<icons.CrownTwoTone style={{ fontSize: '34px' }} />,
		"title":"CrownTwoTone"
	},
	{
		"icon":<icons.CustomerServiceFilled style={{ fontSize: '34px' }} />,
		"title":"CustomerServiceFilled"
	},
	{
		"icon":<icons.CustomerServiceOutlined style={{ fontSize: '34px' }} />,
		"title":"CustomerServiceOutlined"
	},
	{
		"icon":<icons.CustomerServiceTwoTone style={{ fontSize: '34px' }} />,
		"title":"CustomerServiceTwoTone"
	},
	{
		"icon":<icons.DashOutlined style={{ fontSize: '34px' }} />,
		"title":"DashOutlined"
	},
	{
		"icon":<icons.DashboardFilled style={{ fontSize: '34px' }} />,
		"title":"DashboardFilled"
	},
	{
		"icon":<icons.DashboardOutlined style={{ fontSize: '34px' }} />,
		"title":"DashboardOutlined"
	},
	{
		"icon":<icons.DashboardTwoTone style={{ fontSize: '34px' }} />,
		"title":"DashboardTwoTone"
	},
	{
		"icon":<icons.DatabaseFilled style={{ fontSize: '34px' }} />,
		"title":"DatabaseFilled"
	},
	{
		"icon":<icons.DatabaseOutlined style={{ fontSize: '34px' }} />,
		"title":"DatabaseOutlined"
	},
	{
		"icon":<icons.DatabaseTwoTone style={{ fontSize: '34px' }} />,
		"title":"DatabaseTwoTone"
	},
	{
		"icon":<icons.DeleteColumnOutlined style={{ fontSize: '34px' }} />,
		"title":"DeleteColumnOutlined"
	},
	{
		"icon":<icons.DeleteFilled style={{ fontSize: '34px' }} />,
		"title":"DeleteFilled"
	},
	{
		"icon":<icons.DeleteOutlined style={{ fontSize: '34px' }} />,
		"title":"DeleteOutlined"
	},
	{
		"icon":<icons.DeleteRowOutlined style={{ fontSize: '34px' }} />,
		"title":"DeleteRowOutlined"
	},
	{
		"icon":<icons.DeleteTwoTone style={{ fontSize: '34px' }} />,
		"title":"DeleteTwoTone"
	},
	{
		"icon":<icons.DeliveredProcedureOutlined style={{ fontSize: '34px' }} />,
		"title":"DeliveredProcedureOutlined"
	},
	{
		"icon":<icons.DeploymentUnitOutlined style={{ fontSize: '34px' }} />,
		"title":"DeploymentUnitOutlined"
	},
	{
		"icon":<icons.DesktopOutlined style={{ fontSize: '34px' }} />,
		"title":"DesktopOutlined"
	},
	{
		"icon":<icons.DiffFilled style={{ fontSize: '34px' }} />,
		"title":"DiffFilled"
	},
	{
		"icon":<icons.DiffOutlined style={{ fontSize: '34px' }} />,
		"title":"DiffOutlined"
	},
	{
		"icon":<icons.DiffTwoTone style={{ fontSize: '34px' }} />,
		"title":"DiffTwoTone"
	},
	{
		"icon":<icons.DingdingOutlined style={{ fontSize: '34px' }} />,
		"title":"DingdingOutlined"
	},
	{
		"icon":<icons.DingtalkCircleFilled style={{ fontSize: '34px' }} />,
		"title":"DingtalkCircleFilled"
	},
	{
		"icon":<icons.DingtalkOutlined style={{ fontSize: '34px' }} />,
		"title":"DingtalkOutlined"
	},
	{
		"icon":<icons.DingtalkSquareFilled style={{ fontSize: '34px' }} />,
		"title":"DingtalkSquareFilled"
	},
	{
		"icon":<icons.DisconnectOutlined style={{ fontSize: '34px' }} />,
		"title":"DisconnectOutlined"
	},
	{
		"icon":<icons.DiscordFilled style={{ fontSize: '34px' }} />,
		"title":"DiscordFilled"
	},
	{
		"icon":<icons.DiscordOutlined style={{ fontSize: '34px' }} />,
		"title":"DiscordOutlined"
	},
	{
		"icon":<icons.DislikeFilled style={{ fontSize: '34px' }} />,
		"title":"DislikeFilled"
	},
	{
		"icon":<icons.DislikeOutlined style={{ fontSize: '34px' }} />,
		"title":"DislikeOutlined"
	},
	{
		"icon":<icons.DislikeTwoTone style={{ fontSize: '34px' }} />,
		"title":"DislikeTwoTone"
	},
	{
		"icon":<icons.DockerOutlined style={{ fontSize: '34px' }} />,
		"title":"DockerOutlined"
	},
	{
		"icon":<icons.DollarCircleFilled style={{ fontSize: '34px' }} />,
		"title":"DollarCircleFilled"
	},
	{
		"icon":<icons.DollarCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"DollarCircleOutlined"
	},
	{
		"icon":<icons.DollarCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"DollarCircleTwoTone"
	},
	{
		"icon":<icons.DollarOutlined style={{ fontSize: '34px' }} />,
		"title":"DollarOutlined"
	},
	{
		"icon":<icons.DollarTwoTone style={{ fontSize: '34px' }} />,
		"title":"DollarTwoTone"
	},
	{
		"icon":<icons.DotChartOutlined style={{ fontSize: '34px' }} />,
		"title":"DotChartOutlined"
	},
	{
		"icon":<icons.DotNetOutlined style={{ fontSize: '34px' }} />,
		"title":"DotNetOutlined"
	},
	{
		"icon":<icons.DoubleLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"DoubleLeftOutlined"
	},
	{
		"icon":<icons.DoubleRightOutlined style={{ fontSize: '34px' }} />,
		"title":"DoubleRightOutlined"
	},
	{
		"icon":<icons.DownCircleFilled style={{ fontSize: '34px' }} />,
		"title":"DownCircleFilled"
	},
	{
		"icon":<icons.DownCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"DownCircleOutlined"
	},
	{
		"icon":<icons.DownCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"DownCircleTwoTone"
	},
	{
		"icon":<icons.DownOutlined style={{ fontSize: '34px' }} />,
		"title":"DownOutlined"
	},
	{
		"icon":<icons.DownSquareFilled style={{ fontSize: '34px' }} />,
		"title":"DownSquareFilled"
	},
	{
		"icon":<icons.DownSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"DownSquareOutlined"
	},
	{
		"icon":<icons.DownSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"DownSquareTwoTone"
	},
	{
		"icon":<icons.DownloadOutlined style={{ fontSize: '34px' }} />,
		"title":"DownloadOutlined"
	},
	{
		"icon":<icons.DragOutlined style={{ fontSize: '34px' }} />,
		"title":"DragOutlined"
	},
	{
		"icon":<icons.DribbbleCircleFilled style={{ fontSize: '34px' }} />,
		"title":"DribbbleCircleFilled"
	},
	{
		"icon":<icons.DribbbleOutlined style={{ fontSize: '34px' }} />,
		"title":"DribbbleOutlined"
	},
	{
		"icon":<icons.DribbbleSquareFilled style={{ fontSize: '34px' }} />,
		"title":"DribbbleSquareFilled"
	},
	{
		"icon":<icons.DribbbleSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"DribbbleSquareOutlined"
	},
	{
		"icon":<icons.DropboxCircleFilled style={{ fontSize: '34px' }} />,
		"title":"DropboxCircleFilled"
	},
	{
		"icon":<icons.DropboxOutlined style={{ fontSize: '34px' }} />,
		"title":"DropboxOutlined"
	},
	{
		"icon":<icons.DropboxSquareFilled style={{ fontSize: '34px' }} />,
		"title":"DropboxSquareFilled"
	},
	{
		"icon":<icons.EditFilled style={{ fontSize: '34px' }} />,
		"title":"EditFilled"
	},
	{
		"icon":<icons.EditOutlined style={{ fontSize: '34px' }} />,
		"title":"EditOutlined"
	},
	{
		"icon":<icons.EditTwoTone style={{ fontSize: '34px' }} />,
		"title":"EditTwoTone"
	},
	{
		"icon":<icons.EllipsisOutlined style={{ fontSize: '34px' }} />,
		"title":"EllipsisOutlined"
	},
	{
		"icon":<icons.EnterOutlined style={{ fontSize: '34px' }} />,
		"title":"EnterOutlined"
	},
	{
		"icon":<icons.EnvironmentFilled style={{ fontSize: '34px' }} />,
		"title":"EnvironmentFilled"
	},
	{
		"icon":<icons.EnvironmentOutlined style={{ fontSize: '34px' }} />,
		"title":"EnvironmentOutlined"
	},
	{
		"icon":<icons.EnvironmentTwoTone style={{ fontSize: '34px' }} />,
		"title":"EnvironmentTwoTone"
	},
	{
		"icon":<icons.EuroCircleFilled style={{ fontSize: '34px' }} />,
		"title":"EuroCircleFilled"
	},
	{
		"icon":<icons.EuroCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"EuroCircleOutlined"
	},
	{
		"icon":<icons.EuroCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"EuroCircleTwoTone"
	},
	{
		"icon":<icons.EuroOutlined style={{ fontSize: '34px' }} />,
		"title":"EuroOutlined"
	},
	{
		"icon":<icons.EuroTwoTone style={{ fontSize: '34px' }} />,
		"title":"EuroTwoTone"
	},
	{
		"icon":<icons.ExceptionOutlined style={{ fontSize: '34px' }} />,
		"title":"ExceptionOutlined"
	},
	{
		"icon":<icons.ExclamationCircleFilled style={{ fontSize: '34px' }} />,
		"title":"ExclamationCircleFilled"
	},
	{
		"icon":<icons.ExclamationCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"ExclamationCircleOutlined"
	},
	{
		"icon":<icons.ExclamationCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"ExclamationCircleTwoTone"
	},
	{
		"icon":<icons.ExclamationOutlined style={{ fontSize: '34px' }} />,
		"title":"ExclamationOutlined"
	},
	{
		"icon":<icons.ExpandAltOutlined style={{ fontSize: '34px' }} />,
		"title":"ExpandAltOutlined"
	},
	{
		"icon":<icons.ExpandOutlined style={{ fontSize: '34px' }} />,
		"title":"ExpandOutlined"
	},
	{
		"icon":<icons.ExperimentFilled style={{ fontSize: '34px' }} />,
		"title":"ExperimentFilled"
	},
	{
		"icon":<icons.ExperimentOutlined style={{ fontSize: '34px' }} />,
		"title":"ExperimentOutlined"
	},
	{
		"icon":<icons.ExperimentTwoTone style={{ fontSize: '34px' }} />,
		"title":"ExperimentTwoTone"
	},
	{
		"icon":<icons.ExportOutlined style={{ fontSize: '34px' }} />,
		"title":"ExportOutlined"
	},
	{
		"icon":<icons.EyeFilled style={{ fontSize: '34px' }} />,
		"title":"EyeFilled"
	},
	{
		"icon":<icons.EyeInvisibleFilled style={{ fontSize: '34px' }} />,
		"title":"EyeInvisibleFilled"
	},
	{
		"icon":<icons.EyeInvisibleOutlined style={{ fontSize: '34px' }} />,
		"title":"EyeInvisibleOutlined"
	},
	{
		"icon":<icons.EyeInvisibleTwoTone style={{ fontSize: '34px' }} />,
		"title":"EyeInvisibleTwoTone"
	},
	{
		"icon":<icons.EyeOutlined style={{ fontSize: '34px' }} />,
		"title":"EyeOutlined"
	},
	{
		"icon":<icons.EyeTwoTone style={{ fontSize: '34px' }} />,
		"title":"EyeTwoTone"
	},
	{
		"icon":<icons.FacebookFilled style={{ fontSize: '34px' }} />,
		"title":"FacebookFilled"
	},
	{
		"icon":<icons.FacebookOutlined style={{ fontSize: '34px' }} />,
		"title":"FacebookOutlined"
	},
	{
		"icon":<icons.FallOutlined style={{ fontSize: '34px' }} />,
		"title":"FallOutlined"
	},
	{
		"icon":<icons.FastBackwardFilled style={{ fontSize: '34px' }} />,
		"title":"FastBackwardFilled"
	},
	{
		"icon":<icons.FastBackwardOutlined style={{ fontSize: '34px' }} />,
		"title":"FastBackwardOutlined"
	},
	{
		"icon":<icons.FastForwardFilled style={{ fontSize: '34px' }} />,
		"title":"FastForwardFilled"
	},
	{
		"icon":<icons.FastForwardOutlined style={{ fontSize: '34px' }} />,
		"title":"FastForwardOutlined"
	},
	{
		"icon":<icons.FieldBinaryOutlined style={{ fontSize: '34px' }} />,
		"title":"FieldBinaryOutlined"
	},
	{
		"icon":<icons.FieldNumberOutlined style={{ fontSize: '34px' }} />,
		"title":"FieldNumberOutlined"
	},
	{
		"icon":<icons.FieldStringOutlined style={{ fontSize: '34px' }} />,
		"title":"FieldStringOutlined"
	},
	{
		"icon":<icons.FieldTimeOutlined style={{ fontSize: '34px' }} />,
		"title":"FieldTimeOutlined"
	},
	{
		"icon":<icons.FileAddFilled style={{ fontSize: '34px' }} />,
		"title":"FileAddFilled"
	},
	{
		"icon":<icons.FileAddOutlined style={{ fontSize: '34px' }} />,
		"title":"FileAddOutlined"
	},
	{
		"icon":<icons.FileAddTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileAddTwoTone"
	},
	{
		"icon":<icons.FileDoneOutlined style={{ fontSize: '34px' }} />,
		"title":"FileDoneOutlined"
	},
	{
		"icon":<icons.FileExcelFilled style={{ fontSize: '34px' }} />,
		"title":"FileExcelFilled"
	},
	{
		"icon":<icons.FileExcelOutlined style={{ fontSize: '34px' }} />,
		"title":"FileExcelOutlined"
	},
	{
		"icon":<icons.FileExcelTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileExcelTwoTone"
	},
	{
		"icon":<icons.FileExclamationFilled style={{ fontSize: '34px' }} />,
		"title":"FileExclamationFilled"
	},
	{
		"icon":<icons.FileExclamationOutlined style={{ fontSize: '34px' }} />,
		"title":"FileExclamationOutlined"
	},
	{
		"icon":<icons.FileExclamationTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileExclamationTwoTone"
	},
	{
		"icon":<icons.FileFilled style={{ fontSize: '34px' }} />,
		"title":"FileFilled"
	},
	{
		"icon":<icons.FileGifOutlined style={{ fontSize: '34px' }} />,
		"title":"FileGifOutlined"
	},
	{
		"icon":<icons.FileImageFilled style={{ fontSize: '34px' }} />,
		"title":"FileImageFilled"
	},
	{
		"icon":<icons.FileImageOutlined style={{ fontSize: '34px' }} />,
		"title":"FileImageOutlined"
	},
	{
		"icon":<icons.FileImageTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileImageTwoTone"
	},
	{
		"icon":<icons.FileJpgOutlined style={{ fontSize: '34px' }} />,
		"title":"FileJpgOutlined"
	},
	{
		"icon":<icons.FileMarkdownFilled style={{ fontSize: '34px' }} />,
		"title":"FileMarkdownFilled"
	},
	{
		"icon":<icons.FileMarkdownOutlined style={{ fontSize: '34px' }} />,
		"title":"FileMarkdownOutlined"
	},
	{
		"icon":<icons.FileMarkdownTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileMarkdownTwoTone"
	},
	{
		"icon":<icons.FileOutlined style={{ fontSize: '34px' }} />,
		"title":"FileOutlined"
	},
	{
		"icon":<icons.FilePdfFilled style={{ fontSize: '34px' }} />,
		"title":"FilePdfFilled"
	},
	{
		"icon":<icons.FilePdfOutlined style={{ fontSize: '34px' }} />,
		"title":"FilePdfOutlined"
	},
	{
		"icon":<icons.FilePdfTwoTone style={{ fontSize: '34px' }} />,
		"title":"FilePdfTwoTone"
	},
	{
		"icon":<icons.FilePptFilled style={{ fontSize: '34px' }} />,
		"title":"FilePptFilled"
	},
	{
		"icon":<icons.FilePptOutlined style={{ fontSize: '34px' }} />,
		"title":"FilePptOutlined"
	},
	{
		"icon":<icons.FilePptTwoTone style={{ fontSize: '34px' }} />,
		"title":"FilePptTwoTone"
	},
	{
		"icon":<icons.FileProtectOutlined style={{ fontSize: '34px' }} />,
		"title":"FileProtectOutlined"
	},
	{
		"icon":<icons.FileSearchOutlined style={{ fontSize: '34px' }} />,
		"title":"FileSearchOutlined"
	},
	{
		"icon":<icons.FileSyncOutlined style={{ fontSize: '34px' }} />,
		"title":"FileSyncOutlined"
	},
	{
		"icon":<icons.FileTextFilled style={{ fontSize: '34px' }} />,
		"title":"FileTextFilled"
	},
	{
		"icon":<icons.FileTextOutlined style={{ fontSize: '34px' }} />,
		"title":"FileTextOutlined"
	},
	{
		"icon":<icons.FileTextTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileTextTwoTone"
	},
	{
		"icon":<icons.FileTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileTwoTone"
	},
	{
		"icon":<icons.FileUnknownFilled style={{ fontSize: '34px' }} />,
		"title":"FileUnknownFilled"
	},
	{
		"icon":<icons.FileUnknownOutlined style={{ fontSize: '34px' }} />,
		"title":"FileUnknownOutlined"
	},
	{
		"icon":<icons.FileUnknownTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileUnknownTwoTone"
	},
	{
		"icon":<icons.FileWordFilled style={{ fontSize: '34px' }} />,
		"title":"FileWordFilled"
	},
	{
		"icon":<icons.FileWordOutlined style={{ fontSize: '34px' }} />,
		"title":"FileWordOutlined"
	},
	{
		"icon":<icons.FileWordTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileWordTwoTone"
	},
	{
		"icon":<icons.FileZipFilled style={{ fontSize: '34px' }} />,
		"title":"FileZipFilled"
	},
	{
		"icon":<icons.FileZipOutlined style={{ fontSize: '34px' }} />,
		"title":"FileZipOutlined"
	},
	{
		"icon":<icons.FileZipTwoTone style={{ fontSize: '34px' }} />,
		"title":"FileZipTwoTone"
	},
	{
		"icon":<icons.FilterFilled style={{ fontSize: '34px' }} />,
		"title":"FilterFilled"
	},
	{
		"icon":<icons.FilterOutlined style={{ fontSize: '34px' }} />,
		"title":"FilterOutlined"
	},
	{
		"icon":<icons.FilterTwoTone style={{ fontSize: '34px' }} />,
		"title":"FilterTwoTone"
	},
	{
		"icon":<icons.FireFilled style={{ fontSize: '34px' }} />,
		"title":"FireFilled"
	},
	{
		"icon":<icons.FireOutlined style={{ fontSize: '34px' }} />,
		"title":"FireOutlined"
	},
	{
		"icon":<icons.FireTwoTone style={{ fontSize: '34px' }} />,
		"title":"FireTwoTone"
	},
	{
		"icon":<icons.FlagFilled style={{ fontSize: '34px' }} />,
		"title":"FlagFilled"
	},
	{
		"icon":<icons.FlagOutlined style={{ fontSize: '34px' }} />,
		"title":"FlagOutlined"
	},
	{
		"icon":<icons.FlagTwoTone style={{ fontSize: '34px' }} />,
		"title":"FlagTwoTone"
	},
	{
		"icon":<icons.FolderAddFilled style={{ fontSize: '34px' }} />,
		"title":"FolderAddFilled"
	},
	{
		"icon":<icons.FolderAddOutlined style={{ fontSize: '34px' }} />,
		"title":"FolderAddOutlined"
	},
	{
		"icon":<icons.FolderAddTwoTone style={{ fontSize: '34px' }} />,
		"title":"FolderAddTwoTone"
	},
	{
		"icon":<icons.FolderFilled style={{ fontSize: '34px' }} />,
		"title":"FolderFilled"
	},
	{
		"icon":<icons.FolderOpenFilled style={{ fontSize: '34px' }} />,
		"title":"FolderOpenFilled"
	},
	{
		"icon":<icons.FolderOpenOutlined style={{ fontSize: '34px' }} />,
		"title":"FolderOpenOutlined"
	},
	{
		"icon":<icons.FolderOpenTwoTone style={{ fontSize: '34px' }} />,
		"title":"FolderOpenTwoTone"
	},
	{
		"icon":<icons.FolderOutlined style={{ fontSize: '34px' }} />,
		"title":"FolderOutlined"
	},
	{
		"icon":<icons.FolderTwoTone style={{ fontSize: '34px' }} />,
		"title":"FolderTwoTone"
	},
	{
		"icon":<icons.FolderViewOutlined style={{ fontSize: '34px' }} />,
		"title":"FolderViewOutlined"
	},
	{
		"icon":<icons.FontColorsOutlined style={{ fontSize: '34px' }} />,
		"title":"FontColorsOutlined"
	},
	{
		"icon":<icons.FontSizeOutlined style={{ fontSize: '34px' }} />,
		"title":"FontSizeOutlined"
	},
	{
		"icon":<icons.ForkOutlined style={{ fontSize: '34px' }} />,
		"title":"ForkOutlined"
	},
	{
		"icon":<icons.FormOutlined style={{ fontSize: '34px' }} />,
		"title":"FormOutlined"
	},
	{
		"icon":<icons.FormatPainterFilled style={{ fontSize: '34px' }} />,
		"title":"FormatPainterFilled"
	},
	{
		"icon":<icons.FormatPainterOutlined style={{ fontSize: '34px' }} />,
		"title":"FormatPainterOutlined"
	},
	{
		"icon":<icons.ForwardFilled style={{ fontSize: '34px' }} />,
		"title":"ForwardFilled"
	},
	{
		"icon":<icons.ForwardOutlined style={{ fontSize: '34px' }} />,
		"title":"ForwardOutlined"
	},
	{
		"icon":<icons.FrownFilled style={{ fontSize: '34px' }} />,
		"title":"FrownFilled"
	},
	{
		"icon":<icons.FrownOutlined style={{ fontSize: '34px' }} />,
		"title":"FrownOutlined"
	},
	{
		"icon":<icons.FrownTwoTone style={{ fontSize: '34px' }} />,
		"title":"FrownTwoTone"
	},
	{
		"icon":<icons.FullscreenExitOutlined style={{ fontSize: '34px' }} />,
		"title":"FullscreenExitOutlined"
	},
	{
		"icon":<icons.FullscreenOutlined style={{ fontSize: '34px' }} />,
		"title":"FullscreenOutlined"
	},
	{
		"icon":<icons.FunctionOutlined style={{ fontSize: '34px' }} />,
		"title":"FunctionOutlined"
	},
	{
		"icon":<icons.FundFilled style={{ fontSize: '34px' }} />,
		"title":"FundFilled"
	},
	{
		"icon":<icons.FundOutlined style={{ fontSize: '34px' }} />,
		"title":"FundOutlined"
	},
	{
		"icon":<icons.FundProjectionScreenOutlined style={{ fontSize: '34px' }} />,
		"title":"FundProjectionScreenOutlined"
	},
	{
		"icon":<icons.FundTwoTone style={{ fontSize: '34px' }} />,
		"title":"FundTwoTone"
	},
	{
		"icon":<icons.FundViewOutlined style={{ fontSize: '34px' }} />,
		"title":"FundViewOutlined"
	},
	{
		"icon":<icons.FunnelPlotFilled style={{ fontSize: '34px' }} />,
		"title":"FunnelPlotFilled"
	},
	{
		"icon":<icons.FunnelPlotOutlined style={{ fontSize: '34px' }} />,
		"title":"FunnelPlotOutlined"
	},
	{
		"icon":<icons.FunnelPlotTwoTone style={{ fontSize: '34px' }} />,
		"title":"FunnelPlotTwoTone"
	},
	{
		"icon":<icons.GatewayOutlined style={{ fontSize: '34px' }} />,
		"title":"GatewayOutlined"
	},
	{
		"icon":<icons.GifOutlined style={{ fontSize: '34px' }} />,
		"title":"GifOutlined"
	},
	{
		"icon":<icons.GiftFilled style={{ fontSize: '34px' }} />,
		"title":"GiftFilled"
	},
	{
		"icon":<icons.GiftOutlined style={{ fontSize: '34px' }} />,
		"title":"GiftOutlined"
	},
	{
		"icon":<icons.GiftTwoTone style={{ fontSize: '34px' }} />,
		"title":"GiftTwoTone"
	},
	{
		"icon":<icons.GithubFilled style={{ fontSize: '34px' }} />,
		"title":"GithubFilled"
	},
	{
		"icon":<icons.GithubOutlined style={{ fontSize: '34px' }} />,
		"title":"GithubOutlined"
	},
	{
		"icon":<icons.GitlabFilled style={{ fontSize: '34px' }} />,
		"title":"GitlabFilled"
	},
	{
		"icon":<icons.GitlabOutlined style={{ fontSize: '34px' }} />,
		"title":"GitlabOutlined"
	},
	{
		"icon":<icons.GlobalOutlined style={{ fontSize: '34px' }} />,
		"title":"GlobalOutlined"
	},
	{
		"icon":<icons.GoldFilled style={{ fontSize: '34px' }} />,
		"title":"GoldFilled"
	},
	{
		"icon":<icons.GoldOutlined style={{ fontSize: '34px' }} />,
		"title":"GoldOutlined"
	},
	{
		"icon":<icons.GoldTwoTone style={{ fontSize: '34px' }} />,
		"title":"GoldTwoTone"
	},
	{
		"icon":<icons.GoldenFilled style={{ fontSize: '34px' }} />,
		"title":"GoldenFilled"
	},
	{
		"icon":<icons.GoogleCircleFilled style={{ fontSize: '34px' }} />,
		"title":"GoogleCircleFilled"
	},
	{
		"icon":<icons.GoogleOutlined style={{ fontSize: '34px' }} />,
		"title":"GoogleOutlined"
	},
	{
		"icon":<icons.GooglePlusCircleFilled style={{ fontSize: '34px' }} />,
		"title":"GooglePlusCircleFilled"
	},
	{
		"icon":<icons.GooglePlusOutlined style={{ fontSize: '34px' }} />,
		"title":"GooglePlusOutlined"
	},
	{
		"icon":<icons.GooglePlusSquareFilled style={{ fontSize: '34px' }} />,
		"title":"GooglePlusSquareFilled"
	},
	{
		"icon":<icons.GoogleSquareFilled style={{ fontSize: '34px' }} />,
		"title":"GoogleSquareFilled"
	},
	{
		"icon":<icons.GroupOutlined style={{ fontSize: '34px' }} />,
		"title":"GroupOutlined"
	},
	{
		"icon":<icons.HarmonyOSOutlined style={{ fontSize: '34px' }} />,
		"title":"HarmonyOSOutlined"
	},
	{
		"icon":<icons.HddFilled style={{ fontSize: '34px' }} />,
		"title":"HddFilled"
	},
	{
		"icon":<icons.HddOutlined style={{ fontSize: '34px' }} />,
		"title":"HddOutlined"
	},
	{
		"icon":<icons.HddTwoTone style={{ fontSize: '34px' }} />,
		"title":"HddTwoTone"
	},
	{
		"icon":<icons.HeartFilled style={{ fontSize: '34px' }} />,
		"title":"HeartFilled"
	},
	{
		"icon":<icons.HeartOutlined style={{ fontSize: '34px' }} />,
		"title":"HeartOutlined"
	},
	{
		"icon":<icons.HeartTwoTone style={{ fontSize: '34px' }} />,
		"title":"HeartTwoTone"
	},
	{
		"icon":<icons.HeatMapOutlined style={{ fontSize: '34px' }} />,
		"title":"HeatMapOutlined"
	},
	{
		"icon":<icons.HighlightFilled style={{ fontSize: '34px' }} />,
		"title":"HighlightFilled"
	},
	{
		"icon":<icons.HighlightOutlined style={{ fontSize: '34px' }} />,
		"title":"HighlightOutlined"
	},
	{
		"icon":<icons.HighlightTwoTone style={{ fontSize: '34px' }} />,
		"title":"HighlightTwoTone"
	},
	{
		"icon":<icons.HistoryOutlined style={{ fontSize: '34px' }} />,
		"title":"HistoryOutlined"
	},
	{
		"icon":<icons.HolderOutlined style={{ fontSize: '34px' }} />,
		"title":"HolderOutlined"
	},
	{
		"icon":<icons.HomeFilled style={{ fontSize: '34px' }} />,
		"title":"HomeFilled"
	},
	{
		"icon":<icons.HomeOutlined style={{ fontSize: '34px' }} />,
		"title":"HomeOutlined"
	},
	{
		"icon":<icons.HomeTwoTone style={{ fontSize: '34px' }} />,
		"title":"HomeTwoTone"
	},
	{
		"icon":<icons.HourglassFilled style={{ fontSize: '34px' }} />,
		"title":"HourglassFilled"
	},
	{
		"icon":<icons.HourglassOutlined style={{ fontSize: '34px' }} />,
		"title":"HourglassOutlined"
	},
	{
		"icon":<icons.HourglassTwoTone style={{ fontSize: '34px' }} />,
		"title":"HourglassTwoTone"
	},
	{
		"icon":<icons.Html5Filled style={{ fontSize: '34px' }} />,
		"title":"Html5Filled"
	},
	{
		"icon":<icons.Html5Outlined style={{ fontSize: '34px' }} />,
		"title":"Html5Outlined"
	},
	{
		"icon":<icons.Html5TwoTone style={{ fontSize: '34px' }} />,
		"title":"Html5TwoTone"
	},
	{
		"icon":<icons.IdcardFilled style={{ fontSize: '34px' }} />,
		"title":"IdcardFilled"
	},
	{
		"icon":<icons.IdcardOutlined style={{ fontSize: '34px' }} />,
		"title":"IdcardOutlined"
	},
	{
		"icon":<icons.IdcardTwoTone style={{ fontSize: '34px' }} />,
		"title":"IdcardTwoTone"
	},
	{
		"icon":<icons.IeCircleFilled style={{ fontSize: '34px' }} />,
		"title":"IeCircleFilled"
	},
	{
		"icon":<icons.IeOutlined style={{ fontSize: '34px' }} />,
		"title":"IeOutlined"
	},
	{
		"icon":<icons.IeSquareFilled style={{ fontSize: '34px' }} />,
		"title":"IeSquareFilled"
	},
	{
		"icon":<icons.ImportOutlined style={{ fontSize: '34px' }} />,
		"title":"ImportOutlined"
	},
	{
		"icon":<icons.InboxOutlined style={{ fontSize: '34px' }} />,
		"title":"InboxOutlined"
	},
	{
		"icon":<icons.InfoCircleFilled style={{ fontSize: '34px' }} />,
		"title":"InfoCircleFilled"
	},
	{
		"icon":<icons.InfoCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"InfoCircleOutlined"
	},
	{
		"icon":<icons.InfoCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"InfoCircleTwoTone"
	},
	{
		"icon":<icons.InfoOutlined style={{ fontSize: '34px' }} />,
		"title":"InfoOutlined"
	},
	{
		"icon":<icons.InsertRowAboveOutlined style={{ fontSize: '34px' }} />,
		"title":"InsertRowAboveOutlined"
	},
	{
		"icon":<icons.InsertRowBelowOutlined style={{ fontSize: '34px' }} />,
		"title":"InsertRowBelowOutlined"
	},
	{
		"icon":<icons.InsertRowLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"InsertRowLeftOutlined"
	},
	{
		"icon":<icons.InsertRowRightOutlined style={{ fontSize: '34px' }} />,
		"title":"InsertRowRightOutlined"
	},
	{
		"icon":<icons.InstagramFilled style={{ fontSize: '34px' }} />,
		"title":"InstagramFilled"
	},
	{
		"icon":<icons.InstagramOutlined style={{ fontSize: '34px' }} />,
		"title":"InstagramOutlined"
	},
	{
		"icon":<icons.InsuranceFilled style={{ fontSize: '34px' }} />,
		"title":"InsuranceFilled"
	},
	{
		"icon":<icons.InsuranceOutlined style={{ fontSize: '34px' }} />,
		"title":"InsuranceOutlined"
	},
	{
		"icon":<icons.InsuranceTwoTone style={{ fontSize: '34px' }} />,
		"title":"InsuranceTwoTone"
	},
	{
		"icon":<icons.InteractionFilled style={{ fontSize: '34px' }} />,
		"title":"InteractionFilled"
	},
	{
		"icon":<icons.InteractionOutlined style={{ fontSize: '34px' }} />,
		"title":"InteractionOutlined"
	},
	{
		"icon":<icons.InteractionTwoTone style={{ fontSize: '34px' }} />,
		"title":"InteractionTwoTone"
	},
	{
		"icon":<icons.IssuesCloseOutlined style={{ fontSize: '34px' }} />,
		"title":"IssuesCloseOutlined"
	},
	{
		"icon":<icons.ItalicOutlined style={{ fontSize: '34px' }} />,
		"title":"ItalicOutlined"
	},
	{
		"icon":<icons.JavaOutlined style={{ fontSize: '34px' }} />,
		"title":"JavaOutlined"
	},
	{
		"icon":<icons.JavaScriptOutlined style={{ fontSize: '34px' }} />,
		"title":"JavaScriptOutlined"
	},
	{
		"icon":<icons.KeyOutlined style={{ fontSize: '34px' }} />,
		"title":"KeyOutlined"
	},
	{
		"icon":<icons.KubernetesOutlined style={{ fontSize: '34px' }} />,
		"title":"KubernetesOutlined"
	},
	{
		"icon":<icons.LaptopOutlined style={{ fontSize: '34px' }} />,
		"title":"LaptopOutlined"
	},
	{
		"icon":<icons.LayoutFilled style={{ fontSize: '34px' }} />,
		"title":"LayoutFilled"
	},
	{
		"icon":<icons.LayoutOutlined style={{ fontSize: '34px' }} />,
		"title":"LayoutOutlined"
	},
	{
		"icon":<icons.LayoutTwoTone style={{ fontSize: '34px' }} />,
		"title":"LayoutTwoTone"
	},
	{
		"icon":<icons.LeftCircleFilled style={{ fontSize: '34px' }} />,
		"title":"LeftCircleFilled"
	},
	{
		"icon":<icons.LeftCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"LeftCircleOutlined"
	},
	{
		"icon":<icons.LeftCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"LeftCircleTwoTone"
	},
	{
		"icon":<icons.LeftOutlined style={{ fontSize: '34px' }} />,
		"title":"LeftOutlined"
	},
	{
		"icon":<icons.LeftSquareFilled style={{ fontSize: '34px' }} />,
		"title":"LeftSquareFilled"
	},
	{
		"icon":<icons.LeftSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"LeftSquareOutlined"
	},
	{
		"icon":<icons.LeftSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"LeftSquareTwoTone"
	},
	{
		"icon":<icons.LikeFilled style={{ fontSize: '34px' }} />,
		"title":"LikeFilled"
	},
	{
		"icon":<icons.LikeOutlined style={{ fontSize: '34px' }} />,
		"title":"LikeOutlined"
	},
	{
		"icon":<icons.LikeTwoTone style={{ fontSize: '34px' }} />,
		"title":"LikeTwoTone"
	},
	{
		"icon":<icons.LineChartOutlined style={{ fontSize: '34px' }} />,
		"title":"LineChartOutlined"
	},
	{
		"icon":<icons.LineHeightOutlined style={{ fontSize: '34px' }} />,
		"title":"LineHeightOutlined"
	},
	{
		"icon":<icons.LineOutlined style={{ fontSize: '34px' }} />,
		"title":"LineOutlined"
	},
	{
		"icon":<icons.LinkOutlined style={{ fontSize: '34px' }} />,
		"title":"LinkOutlined"
	},
	{
		"icon":<icons.LinkedinFilled style={{ fontSize: '34px' }} />,
		"title":"LinkedinFilled"
	},
	{
		"icon":<icons.LinkedinOutlined style={{ fontSize: '34px' }} />,
		"title":"LinkedinOutlined"
	},
	{
		"icon":<icons.LinuxOutlined style={{ fontSize: '34px' }} />,
		"title":"LinuxOutlined"
	},
	{
		"icon":<icons.Loading3QuartersOutlined style={{ fontSize: '34px' }} />,
		"title":"Loading3QuartersOutlined"
	},
	{
		"icon":<icons.LoadingOutlined style={{ fontSize: '34px' }} />,
		"title":"LoadingOutlined"
	},
	{
		"icon":<icons.LockFilled style={{ fontSize: '34px' }} />,
		"title":"LockFilled"
	},
	{
		"icon":<icons.LockOutlined style={{ fontSize: '34px' }} />,
		"title":"LockOutlined"
	},
	{
		"icon":<icons.LockTwoTone style={{ fontSize: '34px' }} />,
		"title":"LockTwoTone"
	},
	{
		"icon":<icons.LoginOutlined style={{ fontSize: '34px' }} />,
		"title":"LoginOutlined"
	},
	{
		"icon":<icons.LogoutOutlined style={{ fontSize: '34px' }} />,
		"title":"LogoutOutlined"
	},
	{
		"icon":<icons.MacCommandFilled style={{ fontSize: '34px' }} />,
		"title":"MacCommandFilled"
	},
	{
		"icon":<icons.MacCommandOutlined style={{ fontSize: '34px' }} />,
		"title":"MacCommandOutlined"
	},
	{
		"icon":<icons.MailFilled style={{ fontSize: '34px' }} />,
		"title":"MailFilled"
	},
	{
		"icon":<icons.MailOutlined style={{ fontSize: '34px' }} />,
		"title":"MailOutlined"
	},
	{
		"icon":<icons.MailTwoTone style={{ fontSize: '34px' }} />,
		"title":"MailTwoTone"
	},
	{
		"icon":<icons.ManOutlined style={{ fontSize: '34px' }} />,
		"title":"ManOutlined"
	},
	{
		"icon":<icons.MedicineBoxFilled style={{ fontSize: '34px' }} />,
		"title":"MedicineBoxFilled"
	},
	{
		"icon":<icons.MedicineBoxOutlined style={{ fontSize: '34px' }} />,
		"title":"MedicineBoxOutlined"
	},
	{
		"icon":<icons.MedicineBoxTwoTone style={{ fontSize: '34px' }} />,
		"title":"MedicineBoxTwoTone"
	},
	{
		"icon":<icons.MediumCircleFilled style={{ fontSize: '34px' }} />,
		"title":"MediumCircleFilled"
	},
	{
		"icon":<icons.MediumOutlined style={{ fontSize: '34px' }} />,
		"title":"MediumOutlined"
	},
	{
		"icon":<icons.MediumSquareFilled style={{ fontSize: '34px' }} />,
		"title":"MediumSquareFilled"
	},
	{
		"icon":<icons.MediumWorkmarkOutlined style={{ fontSize: '34px' }} />,
		"title":"MediumWorkmarkOutlined"
	},
	{
		"icon":<icons.MehFilled style={{ fontSize: '34px' }} />,
		"title":"MehFilled"
	},
	{
		"icon":<icons.MehOutlined style={{ fontSize: '34px' }} />,
		"title":"MehOutlined"
	},
	{
		"icon":<icons.MehTwoTone style={{ fontSize: '34px' }} />,
		"title":"MehTwoTone"
	},
	{
		"icon":<icons.MenuFoldOutlined style={{ fontSize: '34px' }} />,
		"title":"MenuFoldOutlined"
	},
	{
		"icon":<icons.MenuOutlined style={{ fontSize: '34px' }} />,
		"title":"MenuOutlined"
	},
	{
		"icon":<icons.MenuUnfoldOutlined style={{ fontSize: '34px' }} />,
		"title":"MenuUnfoldOutlined"
	},
	{
		"icon":<icons.MergeCellsOutlined style={{ fontSize: '34px' }} />,
		"title":"MergeCellsOutlined"
	},
	{
		"icon":<icons.MergeFilled style={{ fontSize: '34px' }} />,
		"title":"MergeFilled"
	},
	{
		"icon":<icons.MergeOutlined style={{ fontSize: '34px' }} />,
		"title":"MergeOutlined"
	},
	{
		"icon":<icons.MessageFilled style={{ fontSize: '34px' }} />,
		"title":"MessageFilled"
	},
	{
		"icon":<icons.MessageOutlined style={{ fontSize: '34px' }} />,
		"title":"MessageOutlined"
	},
	{
		"icon":<icons.MessageTwoTone style={{ fontSize: '34px' }} />,
		"title":"MessageTwoTone"
	},
	{
		"icon":<icons.MinusCircleFilled style={{ fontSize: '34px' }} />,
		"title":"MinusCircleFilled"
	},
	{
		"icon":<icons.MinusCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"MinusCircleOutlined"
	},
	{
		"icon":<icons.MinusCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"MinusCircleTwoTone"
	},
	{
		"icon":<icons.MinusOutlined style={{ fontSize: '34px' }} />,
		"title":"MinusOutlined"
	},
	{
		"icon":<icons.MinusSquareFilled style={{ fontSize: '34px' }} />,
		"title":"MinusSquareFilled"
	},
	{
		"icon":<icons.MinusSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"MinusSquareOutlined"
	},
	{
		"icon":<icons.MinusSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"MinusSquareTwoTone"
	},
	{
		"icon":<icons.MobileFilled style={{ fontSize: '34px' }} />,
		"title":"MobileFilled"
	},
	{
		"icon":<icons.MobileOutlined style={{ fontSize: '34px' }} />,
		"title":"MobileOutlined"
	},
	{
		"icon":<icons.MobileTwoTone style={{ fontSize: '34px' }} />,
		"title":"MobileTwoTone"
	},
	{
		"icon":<icons.MoneyCollectFilled style={{ fontSize: '34px' }} />,
		"title":"MoneyCollectFilled"
	},
	{
		"icon":<icons.MoneyCollectOutlined style={{ fontSize: '34px' }} />,
		"title":"MoneyCollectOutlined"
	},
	{
		"icon":<icons.MoneyCollectTwoTone style={{ fontSize: '34px' }} />,
		"title":"MoneyCollectTwoTone"
	},
	{
		"icon":<icons.MonitorOutlined style={{ fontSize: '34px' }} />,
		"title":"MonitorOutlined"
	},
	{
		"icon":<icons.MoonFilled style={{ fontSize: '34px' }} />,
		"title":"MoonFilled"
	},
	{
		"icon":<icons.MoonOutlined style={{ fontSize: '34px' }} />,
		"title":"MoonOutlined"
	},
	{
		"icon":<icons.MoreOutlined style={{ fontSize: '34px' }} />,
		"title":"MoreOutlined"
	},
	{
		"icon":<icons.MutedFilled style={{ fontSize: '34px' }} />,
		"title":"MutedFilled"
	},
	{
		"icon":<icons.MutedOutlined style={{ fontSize: '34px' }} />,
		"title":"MutedOutlined"
	},
	{
		"icon":<icons.NodeCollapseOutlined style={{ fontSize: '34px' }} />,
		"title":"NodeCollapseOutlined"
	},
	{
		"icon":<icons.NodeExpandOutlined style={{ fontSize: '34px' }} />,
		"title":"NodeExpandOutlined"
	},
	{
		"icon":<icons.NodeIndexOutlined style={{ fontSize: '34px' }} />,
		"title":"NodeIndexOutlined"
	},
	{
		"icon":<icons.NotificationFilled style={{ fontSize: '34px' }} />,
		"title":"NotificationFilled"
	},
	{
		"icon":<icons.NotificationOutlined style={{ fontSize: '34px' }} />,
		"title":"NotificationOutlined"
	},
	{
		"icon":<icons.NotificationTwoTone style={{ fontSize: '34px' }} />,
		"title":"NotificationTwoTone"
	},
	{
		"icon":<icons.NumberOutlined style={{ fontSize: '34px' }} />,
		"title":"NumberOutlined"
	},
	{
		"icon":<icons.OneToOneOutlined style={{ fontSize: '34px' }} />,
		"title":"OneToOneOutlined"
	},
	{
		"icon":<icons.OpenAIFilled style={{ fontSize: '34px' }} />,
		"title":"OpenAIFilled"
	},
	{
		"icon":<icons.OpenAIOutlined style={{ fontSize: '34px' }} />,
		"title":"OpenAIOutlined"
	},
	{
		"icon":<icons.OrderedListOutlined style={{ fontSize: '34px' }} />,
		"title":"OrderedListOutlined"
	},
	{
		"icon":<icons.PaperClipOutlined style={{ fontSize: '34px' }} />,
		"title":"PaperClipOutlined"
	},
	{
		"icon":<icons.PartitionOutlined style={{ fontSize: '34px' }} />,
		"title":"PartitionOutlined"
	},
	{
		"icon":<icons.PauseCircleFilled style={{ fontSize: '34px' }} />,
		"title":"PauseCircleFilled"
	},
	{
		"icon":<icons.PauseCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"PauseCircleOutlined"
	},
	{
		"icon":<icons.PauseCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"PauseCircleTwoTone"
	},
	{
		"icon":<icons.PauseOutlined style={{ fontSize: '34px' }} />,
		"title":"PauseOutlined"
	},
	{
		"icon":<icons.PayCircleFilled style={{ fontSize: '34px' }} />,
		"title":"PayCircleFilled"
	},
	{
		"icon":<icons.PayCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"PayCircleOutlined"
	},
	{
		"icon":<icons.PercentageOutlined style={{ fontSize: '34px' }} />,
		"title":"PercentageOutlined"
	},
	{
		"icon":<icons.PhoneFilled style={{ fontSize: '34px' }} />,
		"title":"PhoneFilled"
	},
	{
		"icon":<icons.PhoneOutlined style={{ fontSize: '34px' }} />,
		"title":"PhoneOutlined"
	},
	{
		"icon":<icons.PhoneTwoTone style={{ fontSize: '34px' }} />,
		"title":"PhoneTwoTone"
	},
	{
		"icon":<icons.PicCenterOutlined style={{ fontSize: '34px' }} />,
		"title":"PicCenterOutlined"
	},
	{
		"icon":<icons.PicLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"PicLeftOutlined"
	},
	{
		"icon":<icons.PicRightOutlined style={{ fontSize: '34px' }} />,
		"title":"PicRightOutlined"
	},
	{
		"icon":<icons.PictureFilled style={{ fontSize: '34px' }} />,
		"title":"PictureFilled"
	},
	{
		"icon":<icons.PictureOutlined style={{ fontSize: '34px' }} />,
		"title":"PictureOutlined"
	},
	{
		"icon":<icons.PictureTwoTone style={{ fontSize: '34px' }} />,
		"title":"PictureTwoTone"
	},
	{
		"icon":<icons.PieChartFilled style={{ fontSize: '34px' }} />,
		"title":"PieChartFilled"
	},
	{
		"icon":<icons.PieChartOutlined style={{ fontSize: '34px' }} />,
		"title":"PieChartOutlined"
	},
	{
		"icon":<icons.PieChartTwoTone style={{ fontSize: '34px' }} />,
		"title":"PieChartTwoTone"
	},
	{
		"icon":<icons.PinterestFilled style={{ fontSize: '34px' }} />,
		"title":"PinterestFilled"
	},
	{
		"icon":<icons.PinterestOutlined style={{ fontSize: '34px' }} />,
		"title":"PinterestOutlined"
	},
	{
		"icon":<icons.PlayCircleFilled style={{ fontSize: '34px' }} />,
		"title":"PlayCircleFilled"
	},
	{
		"icon":<icons.PlayCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"PlayCircleOutlined"
	},
	{
		"icon":<icons.PlayCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"PlayCircleTwoTone"
	},
	{
		"icon":<icons.PlaySquareFilled style={{ fontSize: '34px' }} />,
		"title":"PlaySquareFilled"
	},
	{
		"icon":<icons.PlaySquareOutlined style={{ fontSize: '34px' }} />,
		"title":"PlaySquareOutlined"
	},
	{
		"icon":<icons.PlaySquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"PlaySquareTwoTone"
	},
	{
		"icon":<icons.PlusCircleFilled style={{ fontSize: '34px' }} />,
		"title":"PlusCircleFilled"
	},
	{
		"icon":<icons.PlusCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"PlusCircleOutlined"
	},
	{
		"icon":<icons.PlusCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"PlusCircleTwoTone"
	},
	{
		"icon":<icons.PlusOutlined style={{ fontSize: '34px' }} />,
		"title":"PlusOutlined"
	},
	{
		"icon":<icons.PlusSquareFilled style={{ fontSize: '34px' }} />,
		"title":"PlusSquareFilled"
	},
	{
		"icon":<icons.PlusSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"PlusSquareOutlined"
	},
	{
		"icon":<icons.PlusSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"PlusSquareTwoTone"
	},
	{
		"icon":<icons.PoundCircleFilled style={{ fontSize: '34px' }} />,
		"title":"PoundCircleFilled"
	},
	{
		"icon":<icons.PoundCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"PoundCircleOutlined"
	},
	{
		"icon":<icons.PoundCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"PoundCircleTwoTone"
	},
	{
		"icon":<icons.PoundOutlined style={{ fontSize: '34px' }} />,
		"title":"PoundOutlined"
	},
	{
		"icon":<icons.PoweroffOutlined style={{ fontSize: '34px' }} />,
		"title":"PoweroffOutlined"
	},
	{
		"icon":<icons.PrinterFilled style={{ fontSize: '34px' }} />,
		"title":"PrinterFilled"
	},
	{
		"icon":<icons.PrinterOutlined style={{ fontSize: '34px' }} />,
		"title":"PrinterOutlined"
	},
	{
		"icon":<icons.PrinterTwoTone style={{ fontSize: '34px' }} />,
		"title":"PrinterTwoTone"
	},
	{
		"icon":<icons.ProductFilled style={{ fontSize: '34px' }} />,
		"title":"ProductFilled"
	},
	{
		"icon":<icons.ProductOutlined style={{ fontSize: '34px' }} />,
		"title":"ProductOutlined"
	},
	{
		"icon":<icons.ProfileFilled style={{ fontSize: '34px' }} />,
		"title":"ProfileFilled"
	},
	{
		"icon":<icons.ProfileOutlined style={{ fontSize: '34px' }} />,
		"title":"ProfileOutlined"
	},
	{
		"icon":<icons.ProfileTwoTone style={{ fontSize: '34px' }} />,
		"title":"ProfileTwoTone"
	},
	{
		"icon":<icons.ProjectFilled style={{ fontSize: '34px' }} />,
		"title":"ProjectFilled"
	},
	{
		"icon":<icons.ProjectOutlined style={{ fontSize: '34px' }} />,
		"title":"ProjectOutlined"
	},
	{
		"icon":<icons.ProjectTwoTone style={{ fontSize: '34px' }} />,
		"title":"ProjectTwoTone"
	},
	{
		"icon":<icons.PropertySafetyFilled style={{ fontSize: '34px' }} />,
		"title":"PropertySafetyFilled"
	},
	{
		"icon":<icons.PropertySafetyOutlined style={{ fontSize: '34px' }} />,
		"title":"PropertySafetyOutlined"
	},
	{
		"icon":<icons.PropertySafetyTwoTone style={{ fontSize: '34px' }} />,
		"title":"PropertySafetyTwoTone"
	},
	{
		"icon":<icons.PullRequestOutlined style={{ fontSize: '34px' }} />,
		"title":"PullRequestOutlined"
	},
	{
		"icon":<icons.PushpinFilled style={{ fontSize: '34px' }} />,
		"title":"PushpinFilled"
	},
	{
		"icon":<icons.PushpinOutlined style={{ fontSize: '34px' }} />,
		"title":"PushpinOutlined"
	},
	{
		"icon":<icons.PushpinTwoTone style={{ fontSize: '34px' }} />,
		"title":"PushpinTwoTone"
	},
	{
		"icon":<icons.PythonOutlined style={{ fontSize: '34px' }} />,
		"title":"PythonOutlined"
	},
	{
		"icon":<icons.QqCircleFilled style={{ fontSize: '34px' }} />,
		"title":"QqCircleFilled"
	},
	{
		"icon":<icons.QqOutlined style={{ fontSize: '34px' }} />,
		"title":"QqOutlined"
	},
	{
		"icon":<icons.QqSquareFilled style={{ fontSize: '34px' }} />,
		"title":"QqSquareFilled"
	},
	{
		"icon":<icons.QrcodeOutlined style={{ fontSize: '34px' }} />,
		"title":"QrcodeOutlined"
	},
	{
		"icon":<icons.QuestionCircleFilled style={{ fontSize: '34px' }} />,
		"title":"QuestionCircleFilled"
	},
	{
		"icon":<icons.QuestionCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"QuestionCircleOutlined"
	},
	{
		"icon":<icons.QuestionCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"QuestionCircleTwoTone"
	},
	{
		"icon":<icons.QuestionOutlined style={{ fontSize: '34px' }} />,
		"title":"QuestionOutlined"
	},
	{
		"icon":<icons.RadarChartOutlined style={{ fontSize: '34px' }} />,
		"title":"RadarChartOutlined"
	},
	{
		"icon":<icons.RadiusBottomleftOutlined style={{ fontSize: '34px' }} />,
		"title":"RadiusBottomleftOutlined"
	},
	{
		"icon":<icons.RadiusBottomrightOutlined style={{ fontSize: '34px' }} />,
		"title":"RadiusBottomrightOutlined"
	},
	{
		"icon":<icons.RadiusSettingOutlined style={{ fontSize: '34px' }} />,
		"title":"RadiusSettingOutlined"
	},
	{
		"icon":<icons.RadiusUpleftOutlined style={{ fontSize: '34px' }} />,
		"title":"RadiusUpleftOutlined"
	},
	{
		"icon":<icons.RadiusUprightOutlined style={{ fontSize: '34px' }} />,
		"title":"RadiusUprightOutlined"
	},
	{
		"icon":<icons.ReadFilled style={{ fontSize: '34px' }} />,
		"title":"ReadFilled"
	},
	{
		"icon":<icons.ReadOutlined style={{ fontSize: '34px' }} />,
		"title":"ReadOutlined"
	},
	{
		"icon":<icons.ReconciliationFilled style={{ fontSize: '34px' }} />,
		"title":"ReconciliationFilled"
	},
	{
		"icon":<icons.ReconciliationOutlined style={{ fontSize: '34px' }} />,
		"title":"ReconciliationOutlined"
	},
	{
		"icon":<icons.ReconciliationTwoTone style={{ fontSize: '34px' }} />,
		"title":"ReconciliationTwoTone"
	},
	{
		"icon":<icons.RedEnvelopeFilled style={{ fontSize: '34px' }} />,
		"title":"RedEnvelopeFilled"
	},
	{
		"icon":<icons.RedEnvelopeOutlined style={{ fontSize: '34px' }} />,
		"title":"RedEnvelopeOutlined"
	},
	{
		"icon":<icons.RedEnvelopeTwoTone style={{ fontSize: '34px' }} />,
		"title":"RedEnvelopeTwoTone"
	},
	{
		"icon":<icons.RedditCircleFilled style={{ fontSize: '34px' }} />,
		"title":"RedditCircleFilled"
	},
	{
		"icon":<icons.RedditOutlined style={{ fontSize: '34px' }} />,
		"title":"RedditOutlined"
	},
	{
		"icon":<icons.RedditSquareFilled style={{ fontSize: '34px' }} />,
		"title":"RedditSquareFilled"
	},
	{
		"icon":<icons.RedoOutlined style={{ fontSize: '34px' }} />,
		"title":"RedoOutlined"
	},
	{
		"icon":<icons.ReloadOutlined style={{ fontSize: '34px' }} />,
		"title":"ReloadOutlined"
	},
	{
		"icon":<icons.RestFilled style={{ fontSize: '34px' }} />,
		"title":"RestFilled"
	},
	{
		"icon":<icons.RestOutlined style={{ fontSize: '34px' }} />,
		"title":"RestOutlined"
	},
	{
		"icon":<icons.RestTwoTone style={{ fontSize: '34px' }} />,
		"title":"RestTwoTone"
	},
	{
		"icon":<icons.RetweetOutlined style={{ fontSize: '34px' }} />,
		"title":"RetweetOutlined"
	},
	{
		"icon":<icons.RightCircleFilled style={{ fontSize: '34px' }} />,
		"title":"RightCircleFilled"
	},
	{
		"icon":<icons.RightCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"RightCircleOutlined"
	},
	{
		"icon":<icons.RightCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"RightCircleTwoTone"
	},
	{
		"icon":<icons.RightOutlined style={{ fontSize: '34px' }} />,
		"title":"RightOutlined"
	},
	{
		"icon":<icons.RightSquareFilled style={{ fontSize: '34px' }} />,
		"title":"RightSquareFilled"
	},
	{
		"icon":<icons.RightSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"RightSquareOutlined"
	},
	{
		"icon":<icons.RightSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"RightSquareTwoTone"
	},
	{
		"icon":<icons.RiseOutlined style={{ fontSize: '34px' }} />,
		"title":"RiseOutlined"
	},
	{
		"icon":<icons.RobotFilled style={{ fontSize: '34px' }} />,
		"title":"RobotFilled"
	},
	{
		"icon":<icons.RobotOutlined style={{ fontSize: '34px' }} />,
		"title":"RobotOutlined"
	},
	{
		"icon":<icons.RocketFilled style={{ fontSize: '34px' }} />,
		"title":"RocketFilled"
	},
	{
		"icon":<icons.RocketOutlined style={{ fontSize: '34px' }} />,
		"title":"RocketOutlined"
	},
	{
		"icon":<icons.RocketTwoTone style={{ fontSize: '34px' }} />,
		"title":"RocketTwoTone"
	},
	{
		"icon":<icons.RollbackOutlined style={{ fontSize: '34px' }} />,
		"title":"RollbackOutlined"
	},
	{
		"icon":<icons.RotateLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"RotateLeftOutlined"
	},
	{
		"icon":<icons.RotateRightOutlined style={{ fontSize: '34px' }} />,
		"title":"RotateRightOutlined"
	},
	{
		"icon":<icons.RubyOutlined style={{ fontSize: '34px' }} />,
		"title":"RubyOutlined"
	},
	{
		"icon":<icons.SafetyCertificateFilled style={{ fontSize: '34px' }} />,
		"title":"SafetyCertificateFilled"
	},
	{
		"icon":<icons.SafetyCertificateOutlined style={{ fontSize: '34px' }} />,
		"title":"SafetyCertificateOutlined"
	},
	{
		"icon":<icons.SafetyCertificateTwoTone style={{ fontSize: '34px' }} />,
		"title":"SafetyCertificateTwoTone"
	},
	{
		"icon":<icons.SafetyOutlined style={{ fontSize: '34px' }} />,
		"title":"SafetyOutlined"
	},
	{
		"icon":<icons.SaveFilled style={{ fontSize: '34px' }} />,
		"title":"SaveFilled"
	},
	{
		"icon":<icons.SaveOutlined style={{ fontSize: '34px' }} />,
		"title":"SaveOutlined"
	},
	{
		"icon":<icons.SaveTwoTone style={{ fontSize: '34px' }} />,
		"title":"SaveTwoTone"
	},
	{
		"icon":<icons.ScanOutlined style={{ fontSize: '34px' }} />,
		"title":"ScanOutlined"
	},
	{
		"icon":<icons.ScheduleFilled style={{ fontSize: '34px' }} />,
		"title":"ScheduleFilled"
	},
	{
		"icon":<icons.ScheduleOutlined style={{ fontSize: '34px' }} />,
		"title":"ScheduleOutlined"
	},
	{
		"icon":<icons.ScheduleTwoTone style={{ fontSize: '34px' }} />,
		"title":"ScheduleTwoTone"
	},
	{
		"icon":<icons.ScissorOutlined style={{ fontSize: '34px' }} />,
		"title":"ScissorOutlined"
	},
	{
		"icon":<icons.SearchOutlined style={{ fontSize: '34px' }} />,
		"title":"SearchOutlined"
	},
	{
		"icon":<icons.SecurityScanFilled style={{ fontSize: '34px' }} />,
		"title":"SecurityScanFilled"
	},
	{
		"icon":<icons.SecurityScanOutlined style={{ fontSize: '34px' }} />,
		"title":"SecurityScanOutlined"
	},
	{
		"icon":<icons.SecurityScanTwoTone style={{ fontSize: '34px' }} />,
		"title":"SecurityScanTwoTone"
	},
	{
		"icon":<icons.SelectOutlined style={{ fontSize: '34px' }} />,
		"title":"SelectOutlined"
	},
	{
		"icon":<icons.SendOutlined style={{ fontSize: '34px' }} />,
		"title":"SendOutlined"
	},
	{
		"icon":<icons.SettingFilled style={{ fontSize: '34px' }} />,
		"title":"SettingFilled"
	},
	{
		"icon":<icons.SettingOutlined style={{ fontSize: '34px' }} />,
		"title":"SettingOutlined"
	},
	{
		"icon":<icons.SettingTwoTone style={{ fontSize: '34px' }} />,
		"title":"SettingTwoTone"
	},
	{
		"icon":<icons.ShakeOutlined style={{ fontSize: '34px' }} />,
		"title":"ShakeOutlined"
	},
	{
		"icon":<icons.ShareAltOutlined style={{ fontSize: '34px' }} />,
		"title":"ShareAltOutlined"
	},
	{
		"icon":<icons.ShopFilled style={{ fontSize: '34px' }} />,
		"title":"ShopFilled"
	},
	{
		"icon":<icons.ShopOutlined style={{ fontSize: '34px' }} />,
		"title":"ShopOutlined"
	},
	{
		"icon":<icons.ShopTwoTone style={{ fontSize: '34px' }} />,
		"title":"ShopTwoTone"
	},
	{
		"icon":<icons.ShoppingCartOutlined style={{ fontSize: '34px' }} />,
		"title":"ShoppingCartOutlined"
	},
	{
		"icon":<icons.ShoppingFilled style={{ fontSize: '34px' }} />,
		"title":"ShoppingFilled"
	},
	{
		"icon":<icons.ShoppingOutlined style={{ fontSize: '34px' }} />,
		"title":"ShoppingOutlined"
	},
	{
		"icon":<icons.ShoppingTwoTone style={{ fontSize: '34px' }} />,
		"title":"ShoppingTwoTone"
	},
	{
		"icon":<icons.ShrinkOutlined style={{ fontSize: '34px' }} />,
		"title":"ShrinkOutlined"
	},
	{
		"icon":<icons.SignalFilled style={{ fontSize: '34px' }} />,
		"title":"SignalFilled"
	},
	{
		"icon":<icons.SignatureFilled style={{ fontSize: '34px' }} />,
		"title":"SignatureFilled"
	},
	{
		"icon":<icons.SignatureOutlined style={{ fontSize: '34px' }} />,
		"title":"SignatureOutlined"
	},
	{
		"icon":<icons.SisternodeOutlined style={{ fontSize: '34px' }} />,
		"title":"SisternodeOutlined"
	},
	{
		"icon":<icons.SketchCircleFilled style={{ fontSize: '34px' }} />,
		"title":"SketchCircleFilled"
	},
	{
		"icon":<icons.SketchOutlined style={{ fontSize: '34px' }} />,
		"title":"SketchOutlined"
	},
	{
		"icon":<icons.SketchSquareFilled style={{ fontSize: '34px' }} />,
		"title":"SketchSquareFilled"
	},
	{
		"icon":<icons.SkinFilled style={{ fontSize: '34px' }} />,
		"title":"SkinFilled"
	},
	{
		"icon":<icons.SkinOutlined style={{ fontSize: '34px' }} />,
		"title":"SkinOutlined"
	},
	{
		"icon":<icons.SkinTwoTone style={{ fontSize: '34px' }} />,
		"title":"SkinTwoTone"
	},
	{
		"icon":<icons.SkypeFilled style={{ fontSize: '34px' }} />,
		"title":"SkypeFilled"
	},
	{
		"icon":<icons.SkypeOutlined style={{ fontSize: '34px' }} />,
		"title":"SkypeOutlined"
	},
	{
		"icon":<icons.SlackCircleFilled style={{ fontSize: '34px' }} />,
		"title":"SlackCircleFilled"
	},
	{
		"icon":<icons.SlackOutlined style={{ fontSize: '34px' }} />,
		"title":"SlackOutlined"
	},
	{
		"icon":<icons.SlackSquareFilled style={{ fontSize: '34px' }} />,
		"title":"SlackSquareFilled"
	},
	{
		"icon":<icons.SlackSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"SlackSquareOutlined"
	},
	{
		"icon":<icons.SlidersFilled style={{ fontSize: '34px' }} />,
		"title":"SlidersFilled"
	},
	{
		"icon":<icons.SlidersOutlined style={{ fontSize: '34px' }} />,
		"title":"SlidersOutlined"
	},
	{
		"icon":<icons.SlidersTwoTone style={{ fontSize: '34px' }} />,
		"title":"SlidersTwoTone"
	},
	{
		"icon":<icons.SmallDashOutlined style={{ fontSize: '34px' }} />,
		"title":"SmallDashOutlined"
	},
	{
		"icon":<icons.SmileFilled style={{ fontSize: '34px' }} />,
		"title":"SmileFilled"
	},
	{
		"icon":<icons.SmileOutlined style={{ fontSize: '34px' }} />,
		"title":"SmileOutlined"
	},
	{
		"icon":<icons.SmileTwoTone style={{ fontSize: '34px' }} />,
		"title":"SmileTwoTone"
	},
	{
		"icon":<icons.SnippetsFilled style={{ fontSize: '34px' }} />,
		"title":"SnippetsFilled"
	},
	{
		"icon":<icons.SnippetsOutlined style={{ fontSize: '34px' }} />,
		"title":"SnippetsOutlined"
	},
	{
		"icon":<icons.SnippetsTwoTone style={{ fontSize: '34px' }} />,
		"title":"SnippetsTwoTone"
	},
	{
		"icon":<icons.SolutionOutlined style={{ fontSize: '34px' }} />,
		"title":"SolutionOutlined"
	},
	{
		"icon":<icons.SortAscendingOutlined style={{ fontSize: '34px' }} />,
		"title":"SortAscendingOutlined"
	},
	{
		"icon":<icons.SortDescendingOutlined style={{ fontSize: '34px' }} />,
		"title":"SortDescendingOutlined"
	},
	{
		"icon":<icons.SoundFilled style={{ fontSize: '34px' }} />,
		"title":"SoundFilled"
	},
	{
		"icon":<icons.SoundOutlined style={{ fontSize: '34px' }} />,
		"title":"SoundOutlined"
	},
	{
		"icon":<icons.SoundTwoTone style={{ fontSize: '34px' }} />,
		"title":"SoundTwoTone"
	},
	{
		"icon":<icons.SplitCellsOutlined style={{ fontSize: '34px' }} />,
		"title":"SplitCellsOutlined"
	},
	{
		"icon":<icons.SpotifyFilled style={{ fontSize: '34px' }} />,
		"title":"SpotifyFilled"
	},
	{
		"icon":<icons.SpotifyOutlined style={{ fontSize: '34px' }} />,
		"title":"SpotifyOutlined"
	},
	{
		"icon":<icons.StarFilled style={{ fontSize: '34px' }} />,
		"title":"StarFilled"
	},
	{
		"icon":<icons.StarOutlined style={{ fontSize: '34px' }} />,
		"title":"StarOutlined"
	},
	{
		"icon":<icons.StarTwoTone style={{ fontSize: '34px' }} />,
		"title":"StarTwoTone"
	},
	{
		"icon":<icons.StepBackwardFilled style={{ fontSize: '34px' }} />,
		"title":"StepBackwardFilled"
	},
	{
		"icon":<icons.StepBackwardOutlined style={{ fontSize: '34px' }} />,
		"title":"StepBackwardOutlined"
	},
	{
		"icon":<icons.StepForwardFilled style={{ fontSize: '34px' }} />,
		"title":"StepForwardFilled"
	},
	{
		"icon":<icons.StepForwardOutlined style={{ fontSize: '34px' }} />,
		"title":"StepForwardOutlined"
	},
	{
		"icon":<icons.StockOutlined style={{ fontSize: '34px' }} />,
		"title":"StockOutlined"
	},
	{
		"icon":<icons.StopFilled style={{ fontSize: '34px' }} />,
		"title":"StopFilled"
	},
	{
		"icon":<icons.StopOutlined style={{ fontSize: '34px' }} />,
		"title":"StopOutlined"
	},
	{
		"icon":<icons.StopTwoTone style={{ fontSize: '34px' }} />,
		"title":"StopTwoTone"
	},
	{
		"icon":<icons.StrikethroughOutlined style={{ fontSize: '34px' }} />,
		"title":"StrikethroughOutlined"
	},
	{
		"icon":<icons.SubnodeOutlined style={{ fontSize: '34px' }} />,
		"title":"SubnodeOutlined"
	},
	{
		"icon":<icons.SunFilled style={{ fontSize: '34px' }} />,
		"title":"SunFilled"
	},
	{
		"icon":<icons.SunOutlined style={{ fontSize: '34px' }} />,
		"title":"SunOutlined"
	},
	{
		"icon":<icons.SwapLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"SwapLeftOutlined"
	},
	{
		"icon":<icons.SwapOutlined style={{ fontSize: '34px' }} />,
		"title":"SwapOutlined"
	},
	{
		"icon":<icons.SwapRightOutlined style={{ fontSize: '34px' }} />,
		"title":"SwapRightOutlined"
	},
	{
		"icon":<icons.SwitcherFilled style={{ fontSize: '34px' }} />,
		"title":"SwitcherFilled"
	},
	{
		"icon":<icons.SwitcherOutlined style={{ fontSize: '34px' }} />,
		"title":"SwitcherOutlined"
	},
	{
		"icon":<icons.SwitcherTwoTone style={{ fontSize: '34px' }} />,
		"title":"SwitcherTwoTone"
	},
	{
		"icon":<icons.SyncOutlined style={{ fontSize: '34px' }} />,
		"title":"SyncOutlined"
	},
	{
		"icon":<icons.TableOutlined style={{ fontSize: '34px' }} />,
		"title":"TableOutlined"
	},
	{
		"icon":<icons.TabletFilled style={{ fontSize: '34px' }} />,
		"title":"TabletFilled"
	},
	{
		"icon":<icons.TabletOutlined style={{ fontSize: '34px' }} />,
		"title":"TabletOutlined"
	},
	{
		"icon":<icons.TabletTwoTone style={{ fontSize: '34px' }} />,
		"title":"TabletTwoTone"
	},
	{
		"icon":<icons.TagFilled style={{ fontSize: '34px' }} />,
		"title":"TagFilled"
	},
	{
		"icon":<icons.TagOutlined style={{ fontSize: '34px' }} />,
		"title":"TagOutlined"
	},
	{
		"icon":<icons.TagTwoTone style={{ fontSize: '34px' }} />,
		"title":"TagTwoTone"
	},
	{
		"icon":<icons.TagsFilled style={{ fontSize: '34px' }} />,
		"title":"TagsFilled"
	},
	{
		"icon":<icons.TagsOutlined style={{ fontSize: '34px' }} />,
		"title":"TagsOutlined"
	},
	{
		"icon":<icons.TagsTwoTone style={{ fontSize: '34px' }} />,
		"title":"TagsTwoTone"
	},
	{
		"icon":<icons.TaobaoCircleFilled style={{ fontSize: '34px' }} />,
		"title":"TaobaoCircleFilled"
	},
	{
		"icon":<icons.TaobaoCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"TaobaoCircleOutlined"
	},
	{
		"icon":<icons.TaobaoOutlined style={{ fontSize: '34px' }} />,
		"title":"TaobaoOutlined"
	},
	{
		"icon":<icons.TaobaoSquareFilled style={{ fontSize: '34px' }} />,
		"title":"TaobaoSquareFilled"
	},
	{
		"icon":<icons.TeamOutlined style={{ fontSize: '34px' }} />,
		"title":"TeamOutlined"
	},
	{
		"icon":<icons.ThunderboltFilled style={{ fontSize: '34px' }} />,
		"title":"ThunderboltFilled"
	},
	{
		"icon":<icons.ThunderboltOutlined style={{ fontSize: '34px' }} />,
		"title":"ThunderboltOutlined"
	},
	{
		"icon":<icons.ThunderboltTwoTone style={{ fontSize: '34px' }} />,
		"title":"ThunderboltTwoTone"
	},
	{
		"icon":<icons.TikTokFilled style={{ fontSize: '34px' }} />,
		"title":"TikTokFilled"
	},
	{
		"icon":<icons.TikTokOutlined style={{ fontSize: '34px' }} />,
		"title":"TikTokOutlined"
	},
	{
		"icon":<icons.ToTopOutlined style={{ fontSize: '34px' }} />,
		"title":"ToTopOutlined"
	},
	{
		"icon":<icons.ToolFilled style={{ fontSize: '34px' }} />,
		"title":"ToolFilled"
	},
	{
		"icon":<icons.ToolOutlined style={{ fontSize: '34px' }} />,
		"title":"ToolOutlined"
	},
	{
		"icon":<icons.ToolTwoTone style={{ fontSize: '34px' }} />,
		"title":"ToolTwoTone"
	},
	{
		"icon":<icons.TrademarkCircleFilled style={{ fontSize: '34px' }} />,
		"title":"TrademarkCircleFilled"
	},
	{
		"icon":<icons.TrademarkCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"TrademarkCircleOutlined"
	},
	{
		"icon":<icons.TrademarkCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"TrademarkCircleTwoTone"
	},
	{
		"icon":<icons.TrademarkOutlined style={{ fontSize: '34px' }} />,
		"title":"TrademarkOutlined"
	},
	{
		"icon":<icons.TransactionOutlined style={{ fontSize: '34px' }} />,
		"title":"TransactionOutlined"
	},
	{
		"icon":<icons.TranslationOutlined style={{ fontSize: '34px' }} />,
		"title":"TranslationOutlined"
	},
	{
		"icon":<icons.TrophyFilled style={{ fontSize: '34px' }} />,
		"title":"TrophyFilled"
	},
	{
		"icon":<icons.TrophyOutlined style={{ fontSize: '34px' }} />,
		"title":"TrophyOutlined"
	},
	{
		"icon":<icons.TrophyTwoTone style={{ fontSize: '34px' }} />,
		"title":"TrophyTwoTone"
	},
	{
		"icon":<icons.TruckFilled style={{ fontSize: '34px' }} />,
		"title":"TruckFilled"
	},
	{
		"icon":<icons.TruckOutlined style={{ fontSize: '34px' }} />,
		"title":"TruckOutlined"
	},
	{
		"icon":<icons.TwitchFilled style={{ fontSize: '34px' }} />,
		"title":"TwitchFilled"
	},
	{
		"icon":<icons.TwitchOutlined style={{ fontSize: '34px' }} />,
		"title":"TwitchOutlined"
	},
	{
		"icon":<icons.TwitterCircleFilled style={{ fontSize: '34px' }} />,
		"title":"TwitterCircleFilled"
	},
	{
		"icon":<icons.TwitterOutlined style={{ fontSize: '34px' }} />,
		"title":"TwitterOutlined"
	},
	{
		"icon":<icons.TwitterSquareFilled style={{ fontSize: '34px' }} />,
		"title":"TwitterSquareFilled"
	},
	{
		"icon":<icons.UnderlineOutlined style={{ fontSize: '34px' }} />,
		"title":"UnderlineOutlined"
	},
	{
		"icon":<icons.UndoOutlined style={{ fontSize: '34px' }} />,
		"title":"UndoOutlined"
	},
	{
		"icon":<icons.UngroupOutlined style={{ fontSize: '34px' }} />,
		"title":"UngroupOutlined"
	},
	{
		"icon":<icons.UnlockFilled style={{ fontSize: '34px' }} />,
		"title":"UnlockFilled"
	},
	{
		"icon":<icons.UnlockOutlined style={{ fontSize: '34px' }} />,
		"title":"UnlockOutlined"
	},
	{
		"icon":<icons.UnlockTwoTone style={{ fontSize: '34px' }} />,
		"title":"UnlockTwoTone"
	},
	{
		"icon":<icons.UnorderedListOutlined style={{ fontSize: '34px' }} />,
		"title":"UnorderedListOutlined"
	},
	{
		"icon":<icons.UpCircleFilled style={{ fontSize: '34px' }} />,
		"title":"UpCircleFilled"
	},
	{
		"icon":<icons.UpCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"UpCircleOutlined"
	},
	{
		"icon":<icons.UpCircleTwoTone style={{ fontSize: '34px' }} />,
		"title":"UpCircleTwoTone"
	},
	{
		"icon":<icons.UpOutlined style={{ fontSize: '34px' }} />,
		"title":"UpOutlined"
	},
	{
		"icon":<icons.UpSquareFilled style={{ fontSize: '34px' }} />,
		"title":"UpSquareFilled"
	},
	{
		"icon":<icons.UpSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"UpSquareOutlined"
	},
	{
		"icon":<icons.UpSquareTwoTone style={{ fontSize: '34px' }} />,
		"title":"UpSquareTwoTone"
	},
	{
		"icon":<icons.UploadOutlined style={{ fontSize: '34px' }} />,
		"title":"UploadOutlined"
	},
	{
		"icon":<icons.UsbFilled style={{ fontSize: '34px' }} />,
		"title":"UsbFilled"
	},
	{
		"icon":<icons.UsbOutlined style={{ fontSize: '34px' }} />,
		"title":"UsbOutlined"
	},
	{
		"icon":<icons.UsbTwoTone style={{ fontSize: '34px' }} />,
		"title":"UsbTwoTone"
	},
	{
		"icon":<icons.UserAddOutlined style={{ fontSize: '34px' }} />,
		"title":"UserAddOutlined"
	},
	{
		"icon":<icons.UserDeleteOutlined style={{ fontSize: '34px' }} />,
		"title":"UserDeleteOutlined"
	},
	{
		"icon":<icons.UserOutlined style={{ fontSize: '34px' }} />,
		"title":"UserOutlined"
	},
	{
		"icon":<icons.UserSwitchOutlined style={{ fontSize: '34px' }} />,
		"title":"UserSwitchOutlined"
	},
	{
		"icon":<icons.UsergroupAddOutlined style={{ fontSize: '34px' }} />,
		"title":"UsergroupAddOutlined"
	},
	{
		"icon":<icons.UsergroupDeleteOutlined style={{ fontSize: '34px' }} />,
		"title":"UsergroupDeleteOutlined"
	},
	{
		"icon":<icons.VerifiedOutlined style={{ fontSize: '34px' }} />,
		"title":"VerifiedOutlined"
	},
	{
		"icon":<icons.VerticalAlignBottomOutlined style={{ fontSize: '34px' }} />,
		"title":"VerticalAlignBottomOutlined"
	},
	{
		"icon":<icons.VerticalAlignMiddleOutlined style={{ fontSize: '34px' }} />,
		"title":"VerticalAlignMiddleOutlined"
	},
	{
		"icon":<icons.VerticalAlignTopOutlined style={{ fontSize: '34px' }} />,
		"title":"VerticalAlignTopOutlined"
	},
	{
		"icon":<icons.VerticalLeftOutlined style={{ fontSize: '34px' }} />,
		"title":"VerticalLeftOutlined"
	},
	{
		"icon":<icons.VerticalRightOutlined style={{ fontSize: '34px' }} />,
		"title":"VerticalRightOutlined"
	},
	{
		"icon":<icons.VideoCameraAddOutlined style={{ fontSize: '34px' }} />,
		"title":"VideoCameraAddOutlined"
	},
	{
		"icon":<icons.VideoCameraFilled style={{ fontSize: '34px' }} />,
		"title":"VideoCameraFilled"
	},
	{
		"icon":<icons.VideoCameraOutlined style={{ fontSize: '34px' }} />,
		"title":"VideoCameraOutlined"
	},
	{
		"icon":<icons.VideoCameraTwoTone style={{ fontSize: '34px' }} />,
		"title":"VideoCameraTwoTone"
	},
	{
		"icon":<icons.WalletFilled style={{ fontSize: '34px' }} />,
		"title":"WalletFilled"
	},
	{
		"icon":<icons.WalletOutlined style={{ fontSize: '34px' }} />,
		"title":"WalletOutlined"
	},
	{
		"icon":<icons.WalletTwoTone style={{ fontSize: '34px' }} />,
		"title":"WalletTwoTone"
	},
	{
		"icon":<icons.WarningFilled style={{ fontSize: '34px' }} />,
		"title":"WarningFilled"
	},
	{
		"icon":<icons.WarningOutlined style={{ fontSize: '34px' }} />,
		"title":"WarningOutlined"
	},
	{
		"icon":<icons.WarningTwoTone style={{ fontSize: '34px' }} />,
		"title":"WarningTwoTone"
	},
	{
		"icon":<icons.WechatFilled style={{ fontSize: '34px' }} />,
		"title":"WechatFilled"
	},
	{
		"icon":<icons.WechatOutlined style={{ fontSize: '34px' }} />,
		"title":"WechatOutlined"
	},
	{
		"icon":<icons.WechatWorkFilled style={{ fontSize: '34px' }} />,
		"title":"WechatWorkFilled"
	},
	{
		"icon":<icons.WechatWorkOutlined style={{ fontSize: '34px' }} />,
		"title":"WechatWorkOutlined"
	},
	{
		"icon":<icons.WeiboCircleFilled style={{ fontSize: '34px' }} />,
		"title":"WeiboCircleFilled"
	},
	{
		"icon":<icons.WeiboCircleOutlined style={{ fontSize: '34px' }} />,
		"title":"WeiboCircleOutlined"
	},
	{
		"icon":<icons.WeiboOutlined style={{ fontSize: '34px' }} />,
		"title":"WeiboOutlined"
	},
	{
		"icon":<icons.WeiboSquareFilled style={{ fontSize: '34px' }} />,
		"title":"WeiboSquareFilled"
	},
	{
		"icon":<icons.WeiboSquareOutlined style={{ fontSize: '34px' }} />,
		"title":"WeiboSquareOutlined"
	},
	{
		"icon":<icons.WhatsAppOutlined style={{ fontSize: '34px' }} />,
		"title":"WhatsAppOutlined"
	},
	{
		"icon":<icons.WifiOutlined style={{ fontSize: '34px' }} />,
		"title":"WifiOutlined"
	},
	{
		"icon":<icons.WindowsFilled style={{ fontSize: '34px' }} />,
		"title":"WindowsFilled"
	},
	{
		"icon":<icons.WindowsOutlined style={{ fontSize: '34px' }} />,
		"title":"WindowsOutlined"
	},
	{
		"icon":<icons.WomanOutlined style={{ fontSize: '34px' }} />,
		"title":"WomanOutlined"
	},
	{
		"icon":<icons.XFilled style={{ fontSize: '34px' }} />,
		"title":"XFilled"
	},
	{
		"icon":<icons.XOutlined style={{ fontSize: '34px' }} />,
		"title":"XOutlined"
	},
	{
		"icon":<icons.YahooFilled style={{ fontSize: '34px' }} />,
		"title":"YahooFilled"
	},
	{
		"icon":<icons.YahooOutlined style={{ fontSize: '34px' }} />,
		"title":"YahooOutlined"
	},
	{
		"icon":<icons.YoutubeFilled style={{ fontSize: '34px' }} />,
		"title":"YoutubeFilled"
	},
	{
		"icon":<icons.YoutubeOutlined style={{ fontSize: '34px' }} />,
		"title":"YoutubeOutlined"
	},
	{
		"icon":<icons.YuqueFilled style={{ fontSize: '34px' }} />,
		"title":"YuqueFilled"
	},
	{
		"icon":<icons.YuqueOutlined style={{ fontSize: '34px' }} />,
		"title":"YuqueOutlined"
	},
	{
		"icon":<icons.ZhihuCircleFilled style={{ fontSize: '34px' }} />,
		"title":"ZhihuCircleFilled"
	},
	{
		"icon":<icons.ZhihuOutlined style={{ fontSize: '34px' }} />,
		"title":"ZhihuOutlined"
	},
	{
		"icon":<icons.ZhihuSquareFilled style={{ fontSize: '34px' }} />,
		"title":"ZhihuSquareFilled"
	},
	{
		"icon":<icons.ZoomInOutlined style={{ fontSize: '34px' }} />,
		"title":"ZoomInOutlined"
	},
	{
		"icon":<icons.ZoomOutOutlined style={{ fontSize: '34px' }} />,
		"title":"ZoomOutOutlined"
	}
];

const kyIcon: React.FC = () => {
  
  return (
    
    <div>
      {

    iconsArr.map((icon,index)=>{
      return <div key ={index} style={{float:'left'}}> {icon.icon}</div>;
    })
         
    }
    </div>
  );
};
export default kyIcon;




