import {ApiReferenceReact} from '@scalar/api-reference-react'
import '@scalar/api-reference-react/style.css'
import { useIntl } from '@@/exports';

export default () => {
	const intl = useIntl();
	const t = (id: string, values?: Record<string, any>) =>
		intl.formatMessage({ id }, values);
	return (
		<ApiReferenceReact
			configuration={
			[
				{
					sources: [
						{
							title: t('sys.apiDoc.auth'),
							url: "/api-proxy/auth/api/v3/api-docs",
							default: true,
						}
					],
					proxyUrl: "/api-proxy/auth/scalar",
					expandAllResponses: true,
					hideClientButton: true,
					orderRequiredPropertiesFirst: true,
					expandAllModelSections: false,
				},
				{
					sources: [
						{
							title: t('sys.apiDoc.admin'),
							url: "/api-proxy/admin/api/v3/api-docs",
							default: true,
						}
					],
					proxyUrl: "/api-proxy/admin/scalar",
					expandAllResponses: true,
					hideClientButton: true,
					orderRequiredPropertiesFirst: true,
					expandAllModelSections: false,
				}
			]
		}
		/>
	);
};
