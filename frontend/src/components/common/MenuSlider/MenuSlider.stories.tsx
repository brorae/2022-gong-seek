import { Meta, Story } from '@storybook/react';
import MenuSlider, { MenuSliderProps } from '@/components/common/MenuSlider/MenuSlider';

const modalRoot = document.createElement('div');
modalRoot.setAttribute('id', 'menu-slider');
document.body.append(modalRoot);

export default {
	title: 'common/MenuSlider',
	component: MenuSlider,
	decorators: [
		(Story) => (
			<div style={{ width: '320px' }}>
				<Story />
			</div>
		),
	],
} as Meta;

const Template: Story<MenuSliderProps> = (args) => <MenuSlider {...args} />;

export const DefaultMenuSlider = Template.bind({});
DefaultMenuSlider.args = {
	closeSlider: () => {
		console.log('menuslider 닫기');
	},
};
