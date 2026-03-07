import { getIntl } from '@@/exports';

const t = (id: string, values?: Record<string, any>) => {
	return getIntl().formatMessage({ id }, values);
};

export default () => {
	return (
		<div className="global-loading-body">
			<div className="loader">{t('common.loading')}</div>
		</div>
	);
};
